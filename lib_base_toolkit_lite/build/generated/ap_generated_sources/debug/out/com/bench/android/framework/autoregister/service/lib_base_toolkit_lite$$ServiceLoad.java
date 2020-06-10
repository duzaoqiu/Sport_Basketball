package com.bench.android.framework.autoregister.service;

import com.alibaba.android.arouter.facade.model.ServiceDescription;
import com.alibaba.android.arouter.facade.template.ILoadService;
import java.lang.String;
import java.util.Map;

public class lib_base_toolkit_lite$$ServiceLoad implements ILoadService {
  @Override
  public void loadServices(Map<String, ServiceDescription> loadServices) {
    loadServices.put(null,new ServiceDescription("com.bench.android.core.service.CustomLoopService",false));
  }
}
