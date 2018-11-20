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
package com.streamsets.datacollector.creation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.streamsets.datacollector.config.AmazonEMRConfig;
import com.streamsets.datacollector.config.DeliveryGuaranteeChooserValues;
import com.streamsets.datacollector.config.ErrorHandlingChooserValues;
import com.streamsets.datacollector.config.ErrorRecordPolicy;
import com.streamsets.datacollector.config.ErrorRecordPolicyChooserValues;
import com.streamsets.datacollector.config.ExecutionModeChooserValues;
import com.streamsets.datacollector.config.LogLevel;
import com.streamsets.datacollector.config.LogLevelChooserValues;
import com.streamsets.datacollector.config.MemoryLimitExceeded;
import com.streamsets.datacollector.config.MemoryLimitExceededChooserValues;
import com.streamsets.datacollector.config.PipelineGroups;
import com.streamsets.datacollector.config.PipelineLifecycleStageChooserValues;
import com.streamsets.datacollector.config.PipelineState;
import com.streamsets.datacollector.config.PipelineStateChooserValues;
import com.streamsets.datacollector.config.PipelineTestStageChooserValues;
import com.streamsets.datacollector.config.PipelineWebhookConfig;
import com.streamsets.datacollector.config.StatsTargetChooserValues;
import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigDefBean;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.DeliveryGuarantee;
import com.streamsets.pipeline.api.Dependency;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;
import com.streamsets.pipeline.api.ListBeanModel;
import com.streamsets.pipeline.api.MultiValueChooserModel;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.api.ValueChooserModel;

import cn.oge.kkm.container.creation.DisplayTypeChooserValues;

// we are using the annotation for reference purposes only.
// the annotation processor does not work on this maven project
// we have a hardcoded 'datacollector-resource-bundles.json' file in resources
@GenerateResourceBundle
@StageDef(
    version = PipelineConfigBean.VERSION,
    label = "Pipeline",
    upgrader = PipelineConfigUpgrader.class,
    onlineHelpRefUrl = "not applicable"
)
@ConfigGroups(PipelineGroups.class)
public class PipelineConfigBean implements Stage {

  public static final int VERSION = 11;

  public static final String DEFAULT_STATS_AGGREGATOR_LIBRARY_NAME = "streamsets-datacollector-basic-lib";

  public static final String DEFAULT_STATS_AGGREGATOR_STAGE_NAME =
      "com_streamsets_pipeline_stage_destination_devnull_StatsDpmDirectlyDTarget";

  public static final String DEFAULT_STATS_AGGREGATOR_STAGE_VERSION = "1";

  public static final String STATS_DPM_DIRECTLY_TARGET = DEFAULT_STATS_AGGREGATOR_LIBRARY_NAME + "::" +
      DEFAULT_STATS_AGGREGATOR_STAGE_NAME + "::" + DEFAULT_STATS_AGGREGATOR_STAGE_VERSION;

  public static final String STATS_AGGREGATOR_DEFAULT = "streamsets-datacollector-basic-lib" +
      "::com_streamsets_pipeline_stage_destination_devnull_StatsNullDTarget::1";

  private static final String TRASH_TARGET = "streamsets-datacollector-basic-lib" +
      "::com_streamsets_pipeline_stage_destination_devnull_ToErrorNullDTarget::1";

  public static final String DEFAULT_TEST_ORIGIN_LIBRARY_NAME = "streamsets-datacollector-dev-lib";

  public static final String DEFAULT_TEST_ORIGIN_STAGE_NAME =
      "com_streamsets_pipeline_stage_devtest_rawdata_RawDataDSource";

  public static final String DEFAULT_TEST_ORIGIN_STAGE_VERSION = "3";

  public static final String RAW_DATA_ORIGIN = DEFAULT_TEST_ORIGIN_LIBRARY_NAME + "::" +
      DEFAULT_TEST_ORIGIN_STAGE_NAME + "::" + DEFAULT_TEST_ORIGIN_STAGE_VERSION;

  public static final String EDGE_HTTP_URL_DEFAULT = "http://localhost:18633";

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      label = "执行模式",
      defaultValue= "STANDALONE",
      displayPosition = 10
  )
  @ValueChooserModel(ExecutionModeChooserValues.class)
  public ExecutionMode executionMode;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "边缘计算服务地址",
      defaultValue = EDGE_HTTP_URL_DEFAULT,
      displayPosition = 15,
      dependsOn = "executionMode",
      triggeredByValue = {"EDGE"}
  )
  public String edgeHttpUrl = EDGE_HTTP_URL_DEFAULT;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue="AT_LEAST_ONCE",
      label = "发送保护",
      displayPosition = 20
  )
  @ValueChooserModel(DeliveryGuaranteeChooserValues.class)
  public DeliveryGuarantee deliveryGuarantee;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      label = "测试算子",
      description = "用来在预览模式中产生测试数据的算子.",
      defaultValue = RAW_DATA_ORIGIN,
      displayPosition = 21
  )
  @ValueChooserModel(PipelineTestStageChooserValues.class)
  public String testOriginStage;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      label = "启动事件",
      description = "用来处理算法实例启动时的算子.",
      defaultValue = TRASH_TARGET,
      displayPosition = 23,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(PipelineLifecycleStageChooserValues.class)
  public String startEventStage;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      label = "停止事件",
      description = "用来处理算法实例停止时的算子.",
      defaultValue = TRASH_TARGET,
      displayPosition = 26,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(PipelineLifecycleStageChooserValues.class)
  public String stopEventStage;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "true",
      label = "发生错误时重试",
      displayPosition = 30,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public boolean shouldRetry;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "-1",
      label = "重试次数",
      dependsOn = "shouldRetry",
      triggeredByValue = "true",
      description = "最大重试次数. 如果需要无限重试，请设为 -1. 两次重试间隔从15秒成倍增加到5分钟.",
      displayPosition = 30
  )
  public int retryAttempts;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.NUMBER,
      label = "最大实例内存占用 (MB)",
      defaultValue = "${jvm:maxMemoryMB() * 0.85}",
      description = "算法实例能使用的最大内存空间，依赖于JAVA虚拟机的堆栈大小 " +
          ". 默认为堆栈大小的85%，设为0则表示无限制。",
      displayPosition = 60,
      min = 0,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public long memoryLimit;


  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue="LOG",
      label = "当内存超出限制时",
      description = "当实例内存超出限制范围时的执行操作. 提示: 配置报警服务." ,
      displayPosition = 70,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(MemoryLimitExceededChooserValues.class)
  public MemoryLimitExceeded memoryLimitExceeded;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      defaultValue = "[\"RUN_ERROR\", \"STOPPED\", \"FINISHED\"]",
      label = "实例状态发生变化时产生通知",
      description = "当算法实例更改到特定状态时通过邮件通知",
      displayPosition = 75,
      group = "NOTIFICATIONS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @MultiValueChooserModel(PipelineStateChooserValues.class)
  public List<PipelineState> notifyOnStates;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.LIST,
      defaultValue = "[]",
      label = "Email地址",
      description = "Email地址",
      displayPosition = 76,
      group = "NOTIFICATIONS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public List<String> emailIDs;

  @ConfigDef(
      required = false,
      defaultValue = "{}",
      type = ConfigDef.Type.MAP,
      label = "参数",
      displayPosition = 80,
      group = "PARAMETERS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public Map<String, Object> constants;
  
  @ConfigDef(
	      required = false,
	      type = ConfigDef.Type.MODEL,
	      label = "模板参数",
	      displayPosition = 80,
	      group = "PARAMETERS",
	      dependsOn = "executionMode",
	      triggeredByValue = "OGE_TEMPLATE"
	  )
  @ListBeanModel
  public List<TemplateConfig> tempConfigs;
  
  public static class TemplateConfig {
	  @ConfigDef(
			  required = true, 
			  type = ConfigDef.Type.STRING, 
			  label = "变量名",
			  displayPosition=1)
	  public String variable ;
	  
	  @ConfigDef(
			  required = true, 
			  type = ConfigDef.Type.STRING, 
			  label = "显示名称",
			  displayPosition=2)
	  public String title ;
	  
	  @ConfigDef(
			  required = true, 
			  type = ConfigDef.Type.STRING, 
			  label = "默认值",
			  displayPosition=3)
	  public String defaultValue ;
	  
	  @ConfigDef(
			  required = true, 
			  type = ConfigDef.Type.MODEL, 
			  label = "显示类型",
			  displayPosition=3
			  )
	  @ValueChooserModel(DisplayTypeChooserValues.class)
	  public DisplayType displayType ;
	  
	  @ConfigDef(
		      required = false,
		      type = ConfigDef.Type.MAP,
		      label = "可选值",
		      description = "在页面显示为下拉选择框的值",
		      displayPosition = 4,
		      dependsOn = "displayType",
		      triggeredByValue = "OPTION"
		)
	  @ListBeanModel
	  public Map<String,Object> options;
	  
	  @ConfigDef(
		      required = true,
		      type = ConfigDef.Type.NUMBER,
		      defaultValue = "1",
		      label = "显示行",
		      description = "相同值的参数将显示在同一行上",
		      displayPosition = 5
		)
	  	public Integer row;
	  
	  @ConfigDef(
		      required = true,
		      type = ConfigDef.Type.NUMBER,
		      defaultValue = "1",
		      label = "显示列",
		      description = "相同值的参数将显示在同一列上",
		      displayPosition = 6
		)
	  	public Integer column;
  }
  
  
  public enum DisplayType implements Label {
	  TEXT("文本输入"),
	  NUMBER("数字"),
	  CHECK("复选框"),
	  OPTION("下拉选择"),
	  TITLE("标题"),
	  H_LINE("水平分隔线");

	  private final String label;

	  DisplayType(String label) {
	    this.label = label;
	  }

	  @Override
	  public String getLabel() {
	    return label;
	  }

	  @Override
	  public String toString() {
	    return name().toLowerCase();
	  }
	}



  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      label = "错误记录",
      displayPosition = 90,
      group = "BAD_RECORDS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(ErrorHandlingChooserValues.class)
  public String badRecordsHandling;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue="ORIGINAL_RECORD",
      label = "错误记录策略",
      description = "当错误发生时，使用何种策略处理错误记录.",
      displayPosition = 93,
      group = "BAD_RECORDS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(ErrorRecordPolicyChooserValues.class)
  public ErrorRecordPolicy errorRecordPolicy = ErrorRecordPolicy.ORIGINAL_RECORD;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      label = "统计器",
      defaultValue = STATS_DPM_DIRECTLY_TARGET,
      displayPosition = 95,
      group = "STATS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE","OGE_TEMPLATE","CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ValueChooserModel(StatsTargetChooserValues.class)
  public String statsAggregatorStage = STATS_DPM_DIRECTLY_TARGET;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.NUMBER,
      label = "工作者个数",
      description = "Number of workers. 0 to start as many workers as Kafka partitions for topic.",
      defaultValue = "0",
      min = 0,
      displayPosition = 100,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_YARN_STREAMING"}
  )
  public long workerCount;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.NUMBER,
      label = "Worker Memory (MB)",
      defaultValue = "2048",
      displayPosition = 150,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "EMR_BATCH"}
  )
  public long clusterSlaveMemory;


  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Worker Java Options",
      defaultValue = "-XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Dlog4j.debug",
      description = "Add properties as needed. Changes to default settings are not recommended.",
      displayPosition = 110,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "EMR_BATCH"}
  )
  public String clusterSlaveJavaOpts;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MAP,
      defaultValue = "{}",
      label = "Launcher ENV",
      description = "Sets additional environment variables for the cluster launcher",
      displayPosition = 120,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_BATCH", "CLUSTER_YARN_STREAMING"}
  )
  public Map<String, String> clusterLauncherEnv;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Mesos Dispatcher URL",
      description = "URL for service which launches Mesos framework",
      displayPosition = 130,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_MESOS_STREAMING"}
  )
  public String mesosDispatcherURL;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue = "INFO",
      label = "日志级别",
      displayPosition = 140,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"EMR_BATCH"}
  )
  @ValueChooserModel(LogLevelChooserValues.class)
  public LogLevel logLevel;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Checkpoint Configuration Directory",
      description = "An SDC resource directory or symbolic link with HDFS/S3 configuration files core-site.xml and hdfs-site.xml",
      displayPosition = 150,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_MESOS_STREAMING"}
  )
  public String hdfsS3ConfDir;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "0",
      label = "速度限制(记录数 / 每秒)",
      description = "实例中允许的每秒最大记录数。如果不配置或设为0，则表示无限制。",
      displayPosition = 180,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public long rateLimit;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "0",
      label = "最大执行者数",
      description = "该算法实例的最大执行者数据，设为0则表示无限制。",
      min = 0,
      displayPosition = 190,
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  public int maxRunners = 0;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "true",
      label = "失败时创建快照",
      description = "当算法实例在处理过程中发生无法恢复的错误时，将创建无法在错误中处理的记录快照，以供分析追溯。",
      dependencies = @Dependency(
          configName = "executionMode", triggeredByValues = "STANDALONE"
      ),
      displayPosition = 200
  )
  public boolean shouldCreateFailureSnapshot;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "60",
      label = "Runner Idle Time (sec)",
      description = "When pipeline runners are idle for at least this time, run an empty batch through the runner to" +
          " process any events or other time-driven functionality. Value -1 will disable this functionality completely.",
      dependencies = @Dependency(
          configName = "executionMode", triggeredByValues = "STANDALONE"
      ),
      displayPosition = 210
  )
  public long runnerIdleTIme = 60;

  @ConfigDef(required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue = "[]",
      label = "Webhooks",
      description = "Webhooks",
      displayPosition = 210,
      group = "NOTIFICATIONS",
      dependsOn = "executionMode",
      triggeredByValue =  {"STANDALONE", "CLUSTER_BATCH", "CLUSTER_YARN_STREAMING", "CLUSTER_MESOS_STREAMING"}
  )
  @ListBeanModel
  public List<PipelineWebhookConfig> webhookConfigs = Collections.emptyList();

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MAP,
      defaultValue = "{}",
      label = "Extra Spark Configuration",
      description = "Additional Spark Configuration to pass to the spark-submit script, the parameters will be passed " +
          "as --conf <key>=<value>",
      displayPosition = 220,
      group = "CLUSTER",
      dependsOn = "executionMode",
      triggeredByValue = {"CLUSTER_YARN_STREAMING"}
  )
  public Map<String, String> sparkConfigs;

  @ConfigDefBean
  public AmazonEMRConfig amazonEMRConfig;

  @Override
  public List<ConfigIssue> init(Info info, Context context) {
    return Collections.emptyList();
  }

  @Override
  public void destroy() {
  }

}
