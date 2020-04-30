Map<String, List<FirmExecuteConditionExtends> > aa = new HashMap<String, List<FirmExecuteConditionExtends> >();

		//	第一步、先去重。
//		拼接数据先考虑新增，再进行判断，否则会混乱。
		for(FirmExecuteConditionExtends firmExecute2 : firmExecuteConditionList) {
			FirmExecuteConditionExtends newFirm = new FirmExecuteConditionExtends();
				if(aa.get(firmExecute2.getCompanyName())!=null) {
//					List<FirmExecuteConditionExtends> feclist=	aa.get(firmExecute2.getCompanyName());
					newFirm.setCompanyName(firmExecute2.getCompanyName());
					newFirm.setUpdateDay(firmExecute2.getUpdateDay());
					newFirm.setThreshold(firmExecute2.getThreshold());
					newFirm.setEletricDay(firmExecute2.getEletricDay());
					if(Double.parseDouble(firmExecute2.getIsOutline()) < 0) {
						newFirm.setIsOutline("违规");
					}else{
						newFirm.setIsOutline("正常");
					}
					//feclist.add(newFirm);
//					根据key（aa.get(firmExecute2.getCompanyName())）获取对应map的value。
					aa.get(firmExecute2.getCompanyName()).add(newFirm);
					
					//aa.put(firmExecute2.getCompanyName(),feclist);
				}else {
					List<FirmExecuteConditionExtends> newFirmList = new ArrayList<FirmExecuteConditionExtends>();
					
					newFirm.setCompanyName(firmExecute2.getCompanyName());
					newFirm.setUpdateDay(firmExecute2.getUpdateDay());
					newFirm.setThreshold(firmExecute2.getThreshold());
					newFirm.setEletricDay(firmExecute2.getEletricDay());
					if(Double.parseDouble(firmExecute2.getIsOutline()) < 0) {
						newFirm.setIsOutline("违规");
					}else{
						newFirm.setIsOutline("正常");
					}
					newFirmList.add(newFirm);
					aa.put(firmExecute2.getCompanyName(), newFirmList);
						
				}
			
		}
		List<FirmExecuteConditionExtends> childrens = new ArrayList<FirmExecuteConditionExtends>();
		
//		第二步、再往集合中填充
//		知识盲区：Map.Entry<> 和aa.entrySet()
		 for (Map.Entry<String, List<FirmExecuteConditionExtends> > m : aa.entrySet()) {
			 FirmExecuteConditionExtends firmchildren = new FirmExecuteConditionExtends();
	            //System.out.println("key:" + m.getKey() + " value:" + m.getValue());
	          firmchildren.setCompanyName(m.getKey());
	          firmchildren.setChildren(m.getValue());
	          childrens.add(firmchildren);
	        }
		
		return childrens;













		package com.lrhb.controllers.electric;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lrhb.dataaccess.model.electric.ElecEquipmentDetailed;
import com.lrhb.mgrframework.beans.response.IResult;
import com.lrhb.mgrframework.beans.response.PageResultBean;
import com.lrhb.mgrframework.beans.response.ResultBean;
import com.lrhb.mgrframework.utils.CheckUtil;
import com.lrhb.mgrframework.utils.HttpClientUtil;
import com.lrhb.service.api.electric.EquipmentDetailedService;
import com.lrhb.service.beans.electric.ElecEquipmentDetailedRsp;
import com.lrhb.service.beans.electric.EquipmentRsp;
import com.lrhb.utils.DateUtil;
import com.lrhb.utils.list.JedisClientPool;

import lombok.extern.slf4j.Slf4j;


/**
 * @author lxk
 * 20200116
 * 设备状态列表
 */
@Slf4j
@RestController
@RequestMapping("/equipment")
@EnableScheduling
public class EquipmentDetailedController {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	//定时任务 redis获取设备详情记录进行保存
	public static final long cacheTime = 1000 * 60 * 5;// 5分钟
	
	@Autowired
	EquipmentDetailedService detailedService;
	@Autowired
    JedisClientPool redis;
	
	/**
	 * 设备状态列表
	 * @param page
	 * @param limit
	 * @param userName
	 * @return ElecMonitorpoint
	 */
	@RequestMapping(value = "/getEquipmentDetailedList.do", method = RequestMethod.POST)
	public IResult getMonitorpointList(String page, String limit,String countyId,String companyName,String pointName,String networkNum,String eState) {
		return new PageResultBean<Collection<EquipmentRsp>>(
				detailedService.getEquipmentRspList(page, limit, countyId, companyName, pointName, networkNum, eState),
				detailedService.getEquipmentRspCount(countyId, companyName, pointName, networkNum, eState));
	}
	
	/**
	 * 设备状态详情记录
	 * @param page
	 * @param limit
	 * @param companyId
	 * @param pointName
	 * @param companyName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/getEquipmentDetailedPageList.do", method = RequestMethod.POST)
	public IResult getEquipmentDetailedPageList(String page, String limit, 
    		String companyId, String pointName,String companyName,
    		String startTime, String endTime) {
		return new PageResultBean<Collection<ElecEquipmentDetailedRsp>>(
				detailedService.selectEquipmentDetailedPageList(page, limit, companyId, pointName, companyName, startTime, endTime),
				detailedService.selectEquipmentDetailedPageListCount(companyId, pointName, companyName, startTime, endTime));
	}
	
	/**
     * 	获取设备状态详情
     * */
    @RequestMapping(value = "/getdetailed.do",method = RequestMethod.POST)
    @ResponseBody
    public IResult getdetailed(String monitoringPointId){
        return new ResultBean<ElecEquipmentDetailed>(detailedService.getEquipmentById(monitoringPointId));
    }
	/**
	 * 	添加状态详情记录 未用
	 * @param elecEquipmentDetailed
	 * @return
	 */
	@RequestMapping(value = "/addEquipmentDetailed.do",method = RequestMethod.POST)
    public IResult addEquipmentDetailed(ElecEquipmentDetailed elecEquipmentDetailed){
        return new ResultBean<Integer>(detailedService.addEquipmentDetailed(elecEquipmentDetailed));
    }
	
	
	/**
	 * 	添加状态详情记录(2)未用
	 * @param elecEquipmentDetailedRsp
	 * @return
	 */
	@RequestMapping(value = "/saveEquipmentDetailed.do",method = RequestMethod.POST)
    public IResult saveEquipmentDetailed(ElecEquipmentDetailedRsp elecEquipmentDetailedRsp){
		CheckUtil.notBlank(elecEquipmentDetailedRsp.getNetworkNum(),"网关编号字段为空");
		CheckUtil.notBlank(elecEquipmentDetailedRsp.getInstrumentAddress(),"仪表地址字段为空");
        return new ResultBean<Integer>(detailedService.saveEquipmentDetailed(elecEquipmentDetailedRsp));
    }

	/**
	 * 	定时抓取redis数据添加状态详情记录
	 * @param elecEquipmentDetailedRsp
	 * @return
	 */
	@Scheduled(fixedRate = cacheTime)
	@Async
    public void saveDetailed(){
		logger.info("定时开始！抓取redis设备状态详情");
        detailedService.timingSaveEquipmentDetailed();
    }
	
	
	
	public static void main(String[] args) {
//	     String path1 = Thread.currentThread().getContextClassLoader().getResource("").getPath();//获取当前资源的虚拟路径
//
//	      System.out.println(path1);
//		//初始化Spring容器
//				ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
//				//从容器中获得JedisClient对象
//				JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
//				jedisClient.set("first", "100");
//				String result = jedisClient.get("first");
//				System.out.println(result);
//				jedisClient.set("12", "1221");
				
//				Set<String> set  = jedisClient.getKeys("001093101852001040030392-2*");
//				for(String key : set){
//					System.out.println(key);
//				}
				
//				List<String> keys = jedisClient.lrange("monitor_point", 0, -1);
//				for(String key : keys){
//					String [] temp = key.split("-");
//					//拼接key 
//					StringBuffer exceptiondetailKay=new StringBuffer();
//					exceptiondetailKay.append("exceptiondetail-").append(temp[1]).append("-").append(temp[2]);
//					//例：exceptiondetail-001093101852001040030392-1      取数据(P, Pv)
//					String value = jedisClient.get(exceptiondetailKay.toString());
//					String [] exceptiondetail = value.split(",");
//					for (int i = 0; i < exceptiondetail.length; i++) {
//						System.out.println(exceptiondetail[i]);
//					}
//				}
				
//				Set<String> values = jedisClient.getSets("exceptiondetail-001093101852001040030392-1");
//				for(String value : values){
//					System.out.println(value);
//					if(value.equals("P")) {
//						//为P则为功率越限 增加异常类型 和 筛选异常类型为6
//					}else {
//						//Pv 为用电量报警 （用电量越线） 增加异常类型和筛选异常类型为10
//					}
//				}
	    //<!  短信测试
//				Map<String,String> arg = new HashMap<String,String>();
//				//loginName
//				arg.put("company",new String("d德州龙瑞环保"));
//				arg.put("dateTime",DateUtil.date2Str(new Date()));
//				arg.put("msg",new String("监测点：龙瑞干化学生产出现功率越限"));
//	
//				//company-其他；dateTime-时间；msg-其他；
//				Map<String,String> result = SmsSender.sendMessage("13573753761",arg,"德州两山环境咨询有限公司","SMS_182668651");
//				String code = result.get("Code");
//				System.out.println(code);
//				if(!code.equals("OK")) {
//					CheckUtil.check(false, result.get("Message"), "发送短信失败");
//				}
			
				
//				Set<String> keys = jedisClient.getKeys("001093101852001040030392*");
//				
//				for(String key : keys){
//					System.out.println(key);
//					String temp[]=key.split("-");
//					System.out.println(temp[0]+"///"+temp[1]+"///"+ temp[2]);
//					System.out.println(DateUtil.stampToDate(temp[2]));
//					System.out.println(jedisClient.get(key));
//					Map<String, Object> date = JSONObject.fromObject(jedisClient.get(key));
//					System.out.println(date.get("instrument_address"));
//				}
				
//				System.out.println(result);		
	}
	
	/**
     * 	根据企业id获取设备状态详情(总有功功率kw和有功电能kw·h)
     * */
    @RequestMapping(value = "/getDetailBycId.do",method = RequestMethod.POST)
    @ResponseBody
    public IResult getDetailedBycId(String cId, String date){
    	CheckUtil.notBlank(cId,"企业为空");
    	CheckUtil.notBlank(date,"日期不能为空");
    	IResult result = null;
    	//历史数据（非当天、当月、当年）从缓存中获取
    	String todayStr = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
    	if(!StringUtils.isEmpty(date) && !StringUtils.equalsIgnoreCase(todayStr, date)) {
    		IResult cacheResult = redis.getFromCache("getDetailBycId", cId,date);
        	if(cacheResult != null) {
        		result = cacheResult;
        	}else {
        		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getDetailedByCompanyId(cId,date));
        		redis.saveCache(result, "getDetailBycId", cId, date);
        	}
    	}else {
    		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getDetailedByCompanyId(cId,date));
    	}
        return result;
    }
    
    /**
     * 	根据监测点id获取设备状态详情(总有功功率kw和有功电能kw·h)
     * */
    @RequestMapping(value = "/getDetailedByMonitorId.do",method = RequestMethod.POST)
    @ResponseBody
    public IResult getDetailedByMonitorId(String monitorId, String date){
    	CheckUtil.notNullStr(monitorId,"监测点为空");
    	CheckUtil.notBlank(date,"日期不能为空");
    	IResult result = null;
    	//历史数据（非当天、当月、当年）从缓存中获取
    	String todayStr = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
    	if(!StringUtils.isEmpty(date) && !StringUtils.equalsIgnoreCase(todayStr, date)) {
    		IResult cacheResult = redis.getFromCache("getDetailedByMonitorId", monitorId,date);
        	if(cacheResult != null) {
        		result = cacheResult;
        	}else {
        		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getDetailedByMonitorId(monitorId,date));
        		redis.saveCache(result, "getDetailedByMonitorId", monitorId, date);
        	}
    	}else {
    		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getDetailedByMonitorId(monitorId,date));
    	}
        return result;
    }
    
    /**
     * 	根据监测点id和日期获取设备状态详情(电压、电流、功率、时间、状态),
     * */
    @RequestMapping(value = "/getAllDetailedByMonitorId.do",method = RequestMethod.POST)
    @ResponseBody
    public IResult getAllDetailedByMonitorIdAndDate(String monitorId, String date){
    	CheckUtil.notNullStr(monitorId,"监测点为空");
    	CheckUtil.notBlank(date,"日期不能为空");
    	IResult result = null;
    	//历史数据（非当天、当月、当年）从缓存中获取
    	String todayStr = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
    	if(!StringUtils.isEmpty(date) && !StringUtils.equalsIgnoreCase(todayStr, date)) {
    		IResult cacheResult = redis.getFromCache("getAllDetailedByMonitorId", monitorId,date);
        	if(cacheResult != null) {
        		result = cacheResult;
        	}else {
        		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getAllDetailedByMonitorId(monitorId,date));
        		redis.saveCache(result, "getAllDetailedByMonitorId", monitorId, date);
        	}
    	}else {
    		result = new ResultBean<Collection<EquipmentRsp>>(detailedService.getAllDetailedByMonitorId(monitorId, date));
    	}
        return result;
    }
    
    /**
     * 	根据企业id和日期获取设备状态详情(电压、电流、功率、时间、状态),
     * */
    @RequestMapping(value = "/getAllDetailedBycId.do",method = RequestMethod.POST)
    @ResponseBody
    public IResult getAllDetailedBycIdAndDate(String cId, String date){
        return new ResultBean<Collection<EquipmentRsp>>(detailedService.getAllDetailedByCompanyId(cId, date));
    }
    
    /**
     * @Description: 根据监测点id获取企业电力负荷明细
     * @param page
     * @param limit
     * @param companyId
     * @param pointName
     * @param companyName
     * @param startTime
     * @param endTime
     * @author mfy
     * @time 2020年2月18日上午10:39:46
     */
    @RequestMapping(value = "/getEleDetailListByMonitorId.do", method = RequestMethod.POST)
	public IResult getEleDetailListByMonitorId(String page, String limit, 
    		String monitoringPointId, String updateTime) {
		return new PageResultBean<Collection<ElecEquipmentDetailedRsp>>(
				detailedService.selectEleDetailListByMonitorId(page, limit, monitoringPointId, updateTime),
				detailedService.selectEleDetailListByMonitorIdCount(monitoringPointId, updateTime));
	}
    

    /**
     * @Description: 根据监测点id获取企业电力负荷明细 （15分钟）
     * @param page
     * @param limit
     * @param monitoringPointId
     * @param updateTime
     * @author lxk
     * @time 2020年3月7日 20200307
     */
    @RequestMapping(value = "/getEleDetailListByPointId.do", method = RequestMethod.POST)
	public IResult getEleDetailListByPointId(String page, String limit, 
    		String monitoringPointId, String updateTime) {
		return new PageResultBean<Collection<ElecEquipmentDetailedRsp>>(
				detailedService.selectEleDetailListByPointId(page, limit, monitoringPointId, updateTime),
				detailedService.selectEleDetailListByPointIdCount(monitoringPointId, updateTime));
	}
	
}