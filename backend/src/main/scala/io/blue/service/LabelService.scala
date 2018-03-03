package io.blue.service

import scala.io.Source
import scala.collection.JavaConversions._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import io.blue.model._
import io.blue.repository._


@Service
@Transactional
class LabelService @Autowired()(val labelRepository: LabelRepository) {

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def findAll = labelRepository.findAll

  def create(label: Label) = labelRepository.save(label)

  def update(id: Long, label: Label) = {
    var l = labelRepository.findOne(id)
    l.name = label.name
    l.description = label.description
    l.color = label.color
    labelRepository.save(l)
  }

  def delete(id: Long) = {
    var label = labelRepository.findOne(id)
    labelRepository.delete(id)
    label
  }

}
