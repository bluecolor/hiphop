package io.blue.service


import scala.reflect.ClassTag
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import io.blue.exception._
import io.blue.repository.SettingRepository
import io.blue.model._
import io.blue.exception.SystemUserNotAllowedException
import com.fasterxml.jackson.databind.ObjectMapper

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import org.apache.commons.lang3.exception.ExceptionUtils


@Service
@Transactional
class SettingService @Autowired()(val settingRepository: SettingRepository){

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)


  @(Autowired @setter)
  private var userService: UserService = _

  def findByName(name: String) = settingRepository.findByNameIgnoreCase(name)

  def findAll = settingRepository.findAll

  def findOne(id: Long) = settingRepository.findOne(id)

  @throws(classOf[SettingAlreadyExistException])
  def create(setting: Setting) = {
    setting.name = setting.name.trim
    val s = findByName(setting.name)
    if(s != null)
      throw new SettingAlreadyExistException(s"""Setting "${setting.name}" already exists!""")
    settingRepository.save(setting)
  }

  def update(setting: Setting) = {
    var s = settingRepository.findOne(setting.id);
    s.user = userService.findMe
    s.value = setting.value
    settingRepository.save(s)
  }

  def delete(id: Long) = {
    val s = settingRepository.findOne(id)
    settingRepository.delete(id)
    s
  }

  def save(setting: Setting) = {
    var o = findByName(setting.name)
    var s = if(o != null) o else new Setting
    s.user = userService.findMe
    s.value = setting.value
    settingRepository.save(s)
  }

  def findSettingsByName[T](c: T, name: String)(implicit tag: ClassTag[T]): Option[T] = {
    val setting = findByName(name)
    if(setting != null) {
      val m = (new ObjectMapper).readValue(setting.value, tag.runtimeClass)
      Some(m.asInstanceOf[T])
    } else {
      None
    }
  }

  // @Deprecated
  // use findSettingsByName
  def findMailSettings : Option[MailSetting] = {
    val setting = findByName("mail")
    if(setting != null) {
      val m = (new ObjectMapper).readValue(setting.value, classOf[MailSetting]);
      Some(m)
    } else {
      None
    }
  }



  def activeMailService: Boolean = {
    val s = settingRepository.findByNameIgnoreCase("mail")
    if(s == null) {
      (new ObjectMapper).readValue(s.value, classOf[MailSetting]).active
    } else {
      false
    }
  }



}
