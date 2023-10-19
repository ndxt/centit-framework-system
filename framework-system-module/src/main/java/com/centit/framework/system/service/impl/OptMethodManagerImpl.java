package com.centit.framework.system.service.impl;

import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.model.basedata.OptMethod;
import com.centit.framework.system.dao.OptMethodDao;
import com.centit.framework.system.dao.RolePowerDao;
import com.centit.framework.system.service.OptMethodManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service("optMethodManager")
@Transactional
public class OptMethodManagerImpl implements OptMethodManager {

    @Autowired
    @NotNull
    protected OptMethodDao optMethodDao;

    @Autowired
    @NotNull
    protected RolePowerDao rolePowerDao;

    @Override
    public void updateOptMethod(OptMethod o) {
        optMethodDao.updateOptMethod(o);
        CodeRepositoryCache.evictCache("OptMethod");
    }

    @Override
    public List<OptMethod> listOptMethodByOptID(String sOptID) {
        return optMethodDao.listOptMethodByOptID(sOptID);
    }

    @Override
    public String getNextOptCode() {
        return optMethodDao.getNextOptCode();
    }

    @Override
    public List<OptMethod> listObjects() {
        return optMethodDao.listObjectsAll();
    }

    @Override
    public OptMethod getObjectById(String optCode) {
        return optMethodDao.getObjectById(optCode);
    }

    @Override
    public void deleteObjectById(String optCode) {
        optMethodDao.deleteOptMethodById(optCode);
        rolePowerDao.deleteRolePowersByOptCode(optCode);
        CodeRepositoryCache.evictCache("OptMethod");
    }

    @Override
    public String saveNewObject(OptMethod optMethod) {
        optMethodDao.saveNewObject(optMethod);
        CodeRepositoryCache.evictCache("OptMethod");
        return optMethod.getOptCode();
    }

    @Override
    public List<OptMethod> listAllOptMethodByUnit(String topUnit) {
        return optMethodDao.listAllOptMethodByUnit(topUnit);
    }
}
