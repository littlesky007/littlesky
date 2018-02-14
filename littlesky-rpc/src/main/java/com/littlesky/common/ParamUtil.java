package com.littlesky.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.littlesky.configbean.Service;
import com.littlesky.rpc.servlet.ResponseInfo;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParamUtil {
    public static String invokeMethod(String param) throws InvocationTargetException, IllegalAccessException {
        JSONObject paramObj = JSONObject.parseObject(param);
        String methodName = paramObj.getString("MethodName");
        String serviceId = paramObj.getString("ServiceId");
        JSONArray paramTypes = paramObj.getJSONArray("ParamTypes");
        JSONArray paramValues = paramObj.getJSONArray("ParamValues");
        ApplicationContext applicationContext = Service.getApplicationContext();
        Object service = applicationContext.getBean(serviceId);
        Method method = getMethod(service, methodName, paramTypes);

        Class<?>[] clazzes = method.getParameterTypes();

        //如果提供的服务有参数，但是没有传入参数
        if (clazzes != null && clazzes.length > 0) {
            if (paramObj == null) {

                return JSONObject.toJSONString(sendMessage(-1,"get request param not allow null"));
            }
        } else {
            if (paramObj != null) {
                return JSONObject.toJSONString(sendMessage(-1,"get request param allow null"));
            }
        }

        if (method != null) {
            Object[] obj = null;
            if (paramValues != null) {

                obj = new Object[paramValues.size()];
                int i = 0;
                for (Object p : paramValues) {
                    obj[i++] = p;
                }
            }

            //反射调用方法
            Object result = method.invoke(service, obj);
            if (result == null) {
                return null;
                // return JSONObject.toJSONString(sendMessage(1,null));
            } else {
                //sendResponseMessage(resp,1, result.toString())
                return JSONObject.toJSONString(sendMessage(1,result.toString()));
            }

        } else {
            //sendResponseMessage(resp,"get request param not allow null");
            // sendResponseMessage(resp,-1,"not have "+serviceId+" : "+"methodName");
            // return "not have " + serviceId + " : " + "methodName";
            return JSONObject.toJSONString(sendMessage(-1,"not have " + serviceId + " : " + "methodName"));
        }
    }

    private static ResponseInfo sendMessage(int flag, String message)
    {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setFlage(flag);
        if(flag!=-1)
        {
            responseInfo.setReturnInfo(message);
        }
        else
        {
            responseInfo.setErrorInfo(message);
        }
        return responseInfo;
    }
    private static Method getMethod(Object obj,String methodName,JSONArray paramTypes)
    {
        boolean isMatch = true;
        //获取所有的方法，包括父类和接口中继承的方法
        Method[] methods = obj.getClass().getMethods();
        for(Method method : methods)
        {
            if(method.getName().equals(methodName))
            {
                Class<?>[] types = method.getParameterTypes();
                if(types.length == paramTypes.size())
                {
                    for(int i=0;i<types.length;i++)
                    {
                        if(types[i].toString().contains(paramTypes.getString(i)))
                        {
                            continue;
                        }
                        else
                        {
                            isMatch=false;
                            break;
                        }
                    }
                    if(isMatch)
                        return method;
                }
            }
        }
        return null;
    }
}
