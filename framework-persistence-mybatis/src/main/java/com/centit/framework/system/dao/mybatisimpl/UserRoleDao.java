package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRoleDao")
public interface UserRoleDao
    extends com.centit.framework.system.dao.UserRoleDao {

    List<UserRole> getAllUserRolesByUserId(@Param("userCode") String userCode,@Param("userCode") String rolePrefix);
}
