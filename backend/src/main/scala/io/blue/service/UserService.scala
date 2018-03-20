package io.blue.service

import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.core.context.SecurityContextHolder
import com.fasterxml.jackson.databind.ObjectMapper

import io.blue.AppInit
import io.blue.repository.UserRepository
import io.blue.model._
import io.blue.exception._
import io.blue.actor.message.{SendUserPasswordMail,SendNewPasswordMail}
import org.apache.commons.lang3.RandomStringUtils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

@Service
@Transactional
class UserService @Autowired()(val userRepository: UserRepository) extends UserDetailsService {

  @(Autowired @setter)
  private var appInit: AppInit = _

  @(Autowired @setter)
  private var settingService: SettingService = _

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)


	override def loadUserByUsername(username: String): UserDetails = {

    val user = userRepository.findByUsername(username);
    val auth = AuthorityUtils.createAuthorityList( s"ROLE_${user.role}" )

		new org.springframework.security.core.userdetails.User(
      user.username,
      user.password,
			auth)
	}

  def search(exp: String) = userRepository.search(exp)

  def findSystemUser = userRepository.findBySystemTrue

  def findAll = userRepository.findAll

  def findOne(id: Long) = userRepository.findOne(id)

  def findMe  = {
    val username: String = SecurityContextHolder
    .getContext()
    .getAuthentication()
    .getPrincipal().asInstanceOf[org.springframework.security.core.userdetails.User].getUsername()

    userRepository.findByUsername(username)
  }

  def findByNameContainingIgnoreCase(name:String) =
    userRepository.findByNameContainingIgnoreCase(name)


  def count = userRepository.count

  def findByUsername(username: String) = userRepository.findByUsername(username)


  def create(user: User, system: Boolean = false) = {
    val password = if(!system) {
      val s = settingService.findMailSettings
      if(s == None || !s.get.isActive){
        user.username
      } else {
        RandomStringUtils.random(8,true,true)
      }
    } else {
      user.password
    }
    user.options = (new ObjectMapper).writeValueAsString(new UserOptions)
    user.password = new BCryptPasswordEncoder().encode(password)
    val u = userRepository.save(user)
    sendMail(SendUserPasswordMail(u, password))
    log.debug(s"User '${user.username}' created.")
    u
  }

  def sendMail(message: AnyRef) = {
    appInit.system.actorSelection("/user/mail") ! message
  }


  def update(user: User) = {
    var u = userRepository.findOne(user.id)
    u.name = if(user.name != null) user.name else u.name
    u.username = user.username
    u.role = user.role
    u.locked = user.locked
    u.email = if(user.email != null) user.email else u.email
    userRepository.save(u)
  }

  @throws(classOf[UsernameAlreadyExistException])
  def updateProfile(user: User) = {
    var u = userRepository.findByUsernameIgnoreCase(user.username)
    var me = findMe
    if(u.id != me.id && u.username.toLowerCase == me.username.toLowerCase){
      throw UsernameAlreadyExistException(user.username)
    }
    me.name = user.name
    me.username = user.username
    me.email = user.email
    userRepository.save(me)
  }

  def updateOptions(options: String) = {
    var me = findMe
    me.options = options
    userRepository.save(me)
    me
  }


  @throws(classOf[RuntimeException])
  def changePassword(oldp: String, newp: String) = {
    var me = findMe
    if(newp == null || newp.isEmpty){
      throw new RuntimeException("Password can not be empty!")
    }
    val encoder = new BCryptPasswordEncoder
    if(!encoder.matches(oldp, me.password)){
      throw new RuntimeException("Wrong old password!")
    }
    me.password = encoder.encode(newp)
    userRepository.save(me)
  }

  def delete(id: Long): User = {
    val user = userRepository.findOne(id);
    if(user.system){
      throw(new SystemUserNotAllowedException("Can not delete system user."))
    }else{
      userRepository.delete(id);
      user;
    }
  }

  /*!
    this can only be accessed from the shell
  */
  def deleteSystemUser(id: Long): User = {
    val user = userRepository.findOne(id);
    if(user == null){
      throw new RuntimeException(s"'${user.username}'' is not found")
    }else if(user != null && !user.system){
      throw new RuntimeException(s"'${user.username}' is not a system user")
    }else{
      userRepository.delete(id);
      user;
    }
  }

  def forgotPassword(str: String) = {
    var user: User = userRepository.findByUsernameIgnoreCase(str.trim)
    if(user == null){
      val users = userRepository.findByEmailIgnoreCase(str.trim)
      if(users != null && users.length > 0){
        user = users(0)
      }
    }
    if(user == null) {
      throw new UserNotFoundException(str)
    }
    val password = RandomStringUtils.random(8,true,true)
    user.password = new BCryptPasswordEncoder().encode(password)
    val u = userRepository.save(user)
    sendMail(SendNewPasswordMail(u, password))
    u
  }

  def setOption(option: Map[String, String]) = {
    val name = option get "name" match {
      case Some(n: String) => n
      case _ =>
    }
    val value = option get "value" match {
      case Some(v: String) => v
      case _ =>
    }
    var me = findMe
    var options = if(me.options != null) {
      (new ObjectMapper).readValue(me.options, classOf[UserOptions])
    } else {
      new UserOptions
    }

    name match {
      case "footer" => options.footer = value == "true"
      case "deno" => options.deno = value == "true"
      case "miniVariant" => options.miniVariant = value == "true"
      case "sound" => options.sound = value == "true"
      case _ =>
    }
    me.options = (new ObjectMapper).writeValueAsString(options)
    userRepository.save(me)
  }

}
