package cn.oge.kkm.creation;

import com.streamsets.datacollector.creation.PipelineConfigBean.DisplayType;
import com.streamsets.pipeline.api.base.BaseEnumChooserValues;

public class DisplayTypeChooserValues extends BaseEnumChooserValues<DisplayType> {
	  public DisplayTypeChooserValues() {
		    super(DisplayType.class);
	  }
	}