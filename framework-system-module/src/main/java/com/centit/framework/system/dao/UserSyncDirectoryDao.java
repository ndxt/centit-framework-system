package com.centit.framework.system.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.model.basedata.UserSyncDirectory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tian_y
 */
@Repository("userSyncDirectoryDao")
public class UserSyncDirectoryDao extends BaseDaoImpl<UserSyncDirectory, String> {

    public List<UserSyncDirectory> listObjectsAll() {
        return super.listObjects();
    }
}
