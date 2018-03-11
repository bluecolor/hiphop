package io.blue.util

import java.util.zip.{ZipEntry, ZipOutputStream}
import java.io.{File, FileInputStream, FileOutputStream, BufferedOutputStream}
import java.io.File
import java.nio.file._


object DiskUtils {
  def zip(path: String, name: String = null) = {
    val dir = new File(path)
    val fileList = files(dir)
    val zipName = if(name != null) name else s"${dir.getName}.zip"
		val fos = new FileOutputStream(zipName)
    val zos = new ZipOutputStream(fos)

    val buffer: Array[Byte] = Array.ofDim[Byte](1024)

    fileList.foreach { file =>
      val fis = new FileInputStream(file)
      zos.putNextEntry(new ZipEntry(file.getName))
      var sz: Int = fis.read(buffer)
      while (sz > 0) {
        zos.write(buffer, 0, sz)
        sz = fis.read(buffer)
      }
      zos.closeEntry
      fis.close
    }

    zos.close
    fos.close
  }

  def files(path: String): List[File] = files(new File(path))
  def files(dir: File): List[File] = dir.listFiles.toList
}
