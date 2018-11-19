/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.validation;

import com.streamsets.pipeline.api.ErrorCode;
import com.streamsets.pipeline.api.GenerateResourceBundle;

// we are using the annotation for reference purposes only.
// the annotation processor does not work on this maven project
// we have a hardcoded 'datacollector-resource-bundles.json' file in resources
@GenerateResourceBundle
public enum ValidationError implements ErrorCode {
  VALIDATION_0000("Unsupported pipeline schema version '{}'"),

  VALIDATION_0001("算法实例无内容"),
  VALIDATION_0002("实例中包含了未连接的算子. 以下算子未能连接: {}"),
  VALIDATION_0003("第一个算子必须是连接器"),
  VALIDATION_0004("这个算子不能是一个连接器"),
  VALIDATION_0005("算子名称已经定义"),
  VALIDATION_0006("算子定义不存在, 库 '{}', 名称 '{}', 版本 '{}'"),
  VALIDATION_0007("必须为该配置设定值"),
  VALIDATION_0008("无效配置值: '{}'"),
  VALIDATION_0009("配置值应该为'{}'"),
  VALIDATION_0010("输出流 '{}' 已经在 '{}' 算子中定义"),
  VALIDATION_0011("算子已经打开输出流"),
    // Issues are augmented with an array of the open streams in the additionalInfo map property

  VALIDATION_0012("{} 不能连接输入流 '{}'"),
  VALIDATION_0013("{} 不能连接输出流 '{}'"),
  VALIDATION_0014("{} 必须连接输入流"),
  VALIDATION_0015("Stage must have '{}' output stream(s) but has '{}'"),
  VALIDATION_0016("无效的算子名称. 名称必须为以下字符 '{}'"),
  VALIDATION_0017("无效的输入流名称 '{}'. 名称必须为以下字符 '{}'"),
  VALIDATION_0018("无效的输出流名称 '{}'. 名称必须为以下字符 '{}'"),

  VALIDATION_0019("Stream condition at index '{}' is not a map"),
  VALIDATION_0020("Stream condition at index '{}' must have a '{}' entry"),
  VALIDATION_0021("Stream condition at index '{}' entry '{}' cannot be NULL"),
  VALIDATION_0022("Stream condition at index '{}' entry '{}' must be a string"),
  VALIDATION_0023("Stream condition at index '{}' entry '{}' cannot be empty"),

  VALIDATION_0024("Configuration of type Map expressed as a list of key/value pairs cannot have null elements in the " +
                  "list. The element at index '{}' is NULL."),
  VALIDATION_0025("Configuration of type Map expressed as a list of key/value pairs must have the 'key' and 'value'" +
                  "entries in the List's Map elements. The element at index '{}' is does not have those 2 entries"),
  VALIDATION_0026("Configuration of type Map expressed as List of key/value pairs must have Map entries in the List," +
                  "element at index '{}' has '{}'"),

  VALIDATION_0027("Data rule '{}' refers to a stream '{}' which is not found in the pipeline configuration"),
  VALIDATION_0028("Data rule '{}' refers to a stage '{}' which is not found in the pipeline configuration"),

  VALIDATION_0029("Configuration must be a string, instead of a '{}'"),
  VALIDATION_0030("The expression value '{}' must be within '${...}'"),

  VALIDATION_0031("The property '{}' should be a single character"),
  VALIDATION_0032("Stage must have at least one output stream"),
  VALIDATION_0033("Invalid Configuration, {}"),

  VALIDATION_0034("Value for configuration '{}' cannot be greater than '{}'"),
  VALIDATION_0035("Value for configuration '{}' cannot be less than '{}'"),
  VALIDATION_0036("{} cannot have event streams '{}'"),
  VALIDATION_0037("Stage can't be on the main pipeline canvas"),

  //Rule Validation Errors
  VALIDATION_0040("The data rule property '{}' must be defined"),
  VALIDATION_0041("The Sampling Percentage property must have a value between 0 and 100"),
  VALIDATION_0042("Email alert is enabled, but no email is specified"),
  VALIDATION_0043("The value defined for Threshold Value is not a number"),
  VALIDATION_0044("The Threshold Value property must have a value between 0 and 100"),
  VALIDATION_0045("The condition '{}' defined for the data rule is not valid: {}"),
  VALIDATION_0046("The condition must use the following format: '${value()<operator><number>}'"),
  VALIDATION_0047("The condition '{}' defined for the metric alert is not valid"),
  VALIDATION_0050("The property '{}' must be defined for the metric alert"),
  VALIDATION_0051("Unsupported rule definition schema version '{}'"),

  VALIDATION_0060("Define the error record handling for the pipeline"),
  VALIDATION_0061("Define the directory for error record files"),
  VALIDATION_0062("Configured memory limit '{}' is not an integer"),
  VALIDATION_0063("Configured memory limit '{}' is above the maximum allowed: '{}'"),
  VALIDATION_0064("Error resolving memory limit: {}"),

  VALIDATION_0070("Pipeline does not define its execution mode"),
  VALIDATION_0071("Stage '{}' from '{}' library does not support '{}' execution mode"),
  VALIDATION_0072("Data Collector is in standalone mode, cannot run pipeline cluster mode"),
  VALIDATION_0073("Data Collector is in cluster mode, cannot run pipeline standalone mode"),
  VALIDATION_0080("Precondition '{}' must begin with '${' and end with '}'"),
  VALIDATION_0081("Invalid precondition '{}': {}"),
  VALIDATION_0082("Cannot create runner with execution mode '{}', another runner with execution mode '{}'"
            + " is active"),
  VALIDATION_0090("Encountered exception while validating configuration : {}"),
  VALIDATION_0091("Found more than one Target stage that triggers offset commit"),
  VALIDATION_0092("Delivery Guarantee can only be {} if pipeline contains a Target that triggers offset commit"),
  VALIDATION_0093("The pipeline title is empty"),

  // Event related validations
  VALIDATION_0100("Invalid event stream name '{}'. Streams can include the following characters: '{}'"),
  VALIDATION_0101("Stage has more than one event lane"),
  VALIDATION_0102("Stage has configured event lane even though it doesn't produce events"),
  VALIDATION_0103("Stage '{}' has input from both data and event branches of the pipeline. This is not allowed."),
  VALIDATION_0104("Stage has open event streams"),
  VALIDATION_0105("Invalid pipeline lifecycle specification: {}"),
  VALIDATION_0106("Pipeline lifecycle events are not supported in mode: {}"),

  // Service related validations
  VALIDATION_0200("Invalid services declaration, expected definition for '{}', but got '{}'"),
  ;

  private final String msg;

  ValidationError(String msg) {
    this.msg = msg;
  }


  @Override
  public String getCode() {
    return name();
  }

  @Override
  public String getMessage() {
    return msg;
  }

}
