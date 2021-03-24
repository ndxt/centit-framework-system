package com.centit.framework.system.service;

import com.centit.framework.system.po.OptMethod;

import java.util.List;

public interface OptMethodManager{

    List<OptMethod> listOptMethodByOptID(String sOptID);

    String getNextOptCode();

    List<OptMethod> listObjects();

    OptMethod getObjectById(String optCode);

    void updateOptMethod(OptMethod optMethod);

    void deleteObjectById(String optCode);

    String saveNewObject(OptMethod optMethod);

    List<OptMethod> listAllOptMethodByUnit(String topUnit);
}
