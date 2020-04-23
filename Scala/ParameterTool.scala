package com.globalegrow.bigdata.utils

import org.apache.log4j.Logger

/**
  * @ClassName DataDictionary.ParameterTool
  * @author 小和尚
  * @version 1.0.0
  * @Description 此工具类用于解析输入参数，使用者可以自行拓展
  * @createTime 2019年09月19日 11:16:00
  */
object ParameterTool {

  //日志
  val logger = Logger.getLogger(ParameterTool.getClass)
  var map = scala.collection.mutable.HashMap.empty[String, String]

  /**
    * @title fromArgs
    * @description
    * @author 小和尚
    * @param [args]
    * @updateTime 2020/4/23 10:05
    * @return _root_.com.globalegrow.bigdata.utils.ParameterTool.type
    * @throws
    */
  //加载入参
  def fromArgs(args: Array[String]) ={

    //加载参数
    for (arg <- args) {
      val keyValue = arg.split("=")
      map += (keyValue(0) -> keyValue(1))
    }
    ParameterTool
  }

  /**
    * @title getString
    * @description
    * @author 小和尚
    * @param [keyName]
    * @updateTime 2020/4/23 10:05
    * @return _root_.scala.Predef.String
    * @throws
    */
  //获取String类型的入参
  def getString(keyName:String) ={

    if (checkNullParam(keyName)){
       val param = map.get(keyName).get
       param
    }else{
      throw new Exception("缺少输入参数："+keyName)
    }

  }

  //获取String类型的入参，带默认值defaultValue
  def getString(keyName:String,defaultValue:String) ={

    if (!checkNullParam(keyName)){
      logger.info("缺少输入参数："+keyName)
      logger.info("启用默认值:"+defaultValue)
    }
    val param = map.get(keyName).getOrElse(defaultValue)
    param
  }

  //获取Int类型的入参
  def getInt(keyName:String) ={
    if (checkNullParam(keyName)){
      val param = map.get(keyName).get
      param.toInt
    }else{
      throw new Exception("缺少输入参数："+keyName)
    }

  }

  //获取Int类型的入参，带默认值defaultValue
  def getInt(keyName:String,defaultValue:Int) ={
    var param:Int=defaultValue
    if (!checkNullParam(keyName)){
      logger.info("缺少输入参数："+keyName)
      logger.info("启用默认值:"+defaultValue)
    }else{
      param= map.get(keyName).get.toInt
    }
    param
  }

  //校验map中是否含有该key
  def checkNullParam(keyName:String): Boolean ={
    val bool = map.keySet.contains(keyName)
    bool
  }
}
