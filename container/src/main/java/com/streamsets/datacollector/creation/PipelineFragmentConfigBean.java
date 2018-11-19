/*
 * Copyright 2018 StreamSets Inc.
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
package com.streamsets.datacollector.creation;

import java.util.List;
import java.util.Map;

import com.streamsets.datacollector.config.ExecutionModeChooserValues;
import com.streamsets.datacollector.config.PipelineFragmentGroups;
import com.streamsets.datacollector.config.PipelineTestStageChooserValues;
import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.api.ValueChooserModel;

// we are using the annotation for reference purposes only.
// the annotation processor does not work on this maven project
// we have a hardcoded 'datacollector-resource-bundles.json' file in resources
@GenerateResourceBundle
@StageDef(
    version = PipelineFragmentConfigBean.VERSION,
    label = "Pipeline Fragment",
    upgrader = PipelineFragmentConfigUpgrader.class,
    onlineHelpRefUrl = "not applicable"
)
@ConfigGroups(PipelineFragmentGroups.class)
public class PipelineFragmentConfigBean implements Stage {



public static final int VERSION = 2;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      label = "运行模式",
      defaultValue= "STANDALONE",
      displayPosition = 10
  )
  @ValueChooserModel(ExecutionModeChooserValues.class)
  public ExecutionMode executionMode;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      label = "测试算子",
      description = "用于在预览模式中提供测试数据.",
      defaultValue = PipelineConfigBean.RAW_DATA_ORIGIN,
      displayPosition = 21
  )
  @ValueChooserModel(PipelineTestStageChooserValues.class)
  public String testOriginStage;

  @ConfigDef(
      required = false,
      defaultValue = "{}",
      type = ConfigDef.Type.MAP,
      label = "参数",
      displayPosition = 80,
      group = "PARAMETERS"
  )
  public Map<String, Object> constants;
  
  

  @Override
  public List<ConfigIssue> init(Info info, Context context) {
    return null;
  }

  @Override
  public void destroy() {
  }
}
