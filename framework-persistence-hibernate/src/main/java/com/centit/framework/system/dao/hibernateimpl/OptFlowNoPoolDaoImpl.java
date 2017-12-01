package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.OptFlowNoPoolDao;
import com.centit.framework.system.po.OptFlowNoPool;
import com.centit.framework.system.po.OptFlowNoPoolId;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository("optFlowNoPoolDao")
public class OptFlowNoPoolDaoImpl extends BaseDaoImpl<OptFlowNoPool, OptFlowNoPoolId>
  implements OptFlowNoPoolDao {


      public static final Logger logger = LoggerFactory.getLogger(OptFlowNoPoolDaoImpl.class);

      public Map<String, String> getFilterField() {
          if (filterField == null) {
              filterField = new HashMap<String, String>();

              filterField.put("ownerCode", "cid.ownerCode = :ownerCode");
              filterField.put("codeDate", "cid.codeDate = :codeDate");
              filterField.put("codeCode", "cid.codeCode = :codeCode");
              filterField.put("curNo", "cid.ownerCode = :curNo");
          }
          return filterField;
      }

      @Transactional
      public long fetchFirstLsh(String ownerCode, String codeCode,
                                Date codeBaseDate) {
          return DatabaseOptUtils.getSingleIntBySql(this,
                  "select min(CURNO) as MinNo from F_OPTFLOWNOPOOL" +
                  " where OWNERCODE = " + QueryUtils.buildStringForQuery(ownerCode) +
                  " and CODECODE = " + QueryUtils.buildStringForQuery(ownerCode) +
                  " and CODEDATE = (date)" + QueryUtils.buildStringForQuery(
                  DatetimeOpt.convertDatetimeToString(codeBaseDate)));
      }

      @Override
      public void saveNewOptFlowNoPool(OptFlowNoPool optFlowNoPool){
          super.saveNewObject(optFlowNoPool);
      }

      @Override
      public void updateOptFlowNoPool(OptFlowNoPool optFlowNoPool){
          super.updateObject(optFlowNoPool);
      }
}
