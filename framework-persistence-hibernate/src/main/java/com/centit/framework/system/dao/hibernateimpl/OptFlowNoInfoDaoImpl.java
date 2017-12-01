package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.system.dao.OptFlowNoInfoDao;
import com.centit.framework.system.po.OptFlowNoInfo;
import com.centit.framework.system.po.OptFlowNoInfoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("optFlowNoInfoDao")
public class OptFlowNoInfoDaoImpl extends BaseDaoImpl<OptFlowNoInfo, OptFlowNoInfoId>
  implements OptFlowNoInfoDao {

    public static final Logger logger = LoggerFactory.getLogger(OptFlowNoInfoDaoImpl.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("ownerCode", "cid.ownerCode = :ownerCode");

            filterField.put("codeDate", CodeBook.EQUAL_HQL_ID);

            filterField.put("codeCode", "cid.codeCode = :codeCode");

            filterField.put("curNo", CodeBook.LIKE_HQL_ID);

            filterField.put("lastCodeDate", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }



      @Override
      public void saveNewOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.saveNewObject(optFlowNoInfo);
      }

      @Override
      public void updateOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.updateObject(optFlowNoInfo);
      }

}
