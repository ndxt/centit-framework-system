package com.centit.framework.system.service;

import com.centit.framework.system.po.OptMethod;

import java.util.List;

public interface OptMethodManager{

    public List<OptMethod> listOptMethodByOptID(String sOptID);

    String getNextOptCode();

    public List<OptMethod> listObjects();

    public OptMethod getObjectById(String optCode);

    public void updateOptMethod(OptMethod optMethod);

    public void deleteObjectById(String optCode);

    public String saveNewObject(OptMethod optMethod);

}
