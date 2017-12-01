package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptFlowNoInfo;
import com.centit.framework.system.po.OptFlowNoInfoId;

public interface OptFlowNoInfoDao {

  /**
   * 根据ID查询
   * @param cid
   * @return
   */
    OptFlowNoInfo getObjectById(OptFlowNoInfoId cid);

  /**
   * 删除
   * @param cid
   */
    void deleteObjectById(OptFlowNoInfoId cid);

  /**
   *
   * @param optFlowNoINfo
   */
  void saveObject(OptFlowNoInfo optFlowNoINfo);

  /**
   * 新增
   * @param optFlowNoInfo
   */
  void saveNewOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo);

  /**
   *  更新
   * @param optFlowNoInfo
   */
  void updateOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo);
}
