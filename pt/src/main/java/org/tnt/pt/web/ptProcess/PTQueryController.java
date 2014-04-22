package org.tnt.pt.web.ptProcess;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tnt.pt.dmsentity.User;
import org.tnt.pt.entity.Business;
import org.tnt.pt.entity.Consignment;
import org.tnt.pt.entity.Country;
import org.tnt.pt.entity.Customer;
import org.tnt.pt.entity.Discount;
import org.tnt.pt.entity.Exam;
import org.tnt.pt.entity.GEOSummary;
import org.tnt.pt.entity.HWRate;
import org.tnt.pt.entity.PTSLoad;
import org.tnt.pt.entity.Rate;
import org.tnt.pt.entity.Tariff;
import org.tnt.pt.entity.TariffGroup;
import org.tnt.pt.entity.WeightBand;
import org.tnt.pt.entity.ZoneGroup;
import org.tnt.pt.entity.ZoneSummary;
import org.tnt.pt.entity.ZoneTable;
import org.tnt.pt.entity.ZoneType;
import org.tnt.pt.service.account.AccountService;
import org.tnt.pt.service.baseInfo.BsmRgmService;
import org.tnt.pt.service.baseInfo.CountryGeoService;
import org.tnt.pt.service.baseInfo.CountryZoneService;
import org.tnt.pt.service.baseInfo.ProductService;
import org.tnt.pt.service.baseInfo.TariffGroupService;
import org.tnt.pt.service.baseInfo.TariffService;
import org.tnt.pt.service.baseInfo.WeightBandService;
import org.tnt.pt.service.baseInfo.ZoneGroupService;
import org.tnt.pt.service.baseInfo.ZoneTypeService;
import org.tnt.pt.service.ptProcess.BusinessService;
import org.tnt.pt.service.ptProcess.ConsignmentService;
import org.tnt.pt.service.ptProcess.CustomerService;
import org.tnt.pt.service.ptProcess.DiscountService;
import org.tnt.pt.service.ptProcess.ExamService;
import org.tnt.pt.service.ptProcess.GeoSummaryService;
import org.tnt.pt.service.ptProcess.HWRateService;
import org.tnt.pt.service.ptProcess.RateService;
import org.tnt.pt.service.ptProcess.RevService;
import org.tnt.pt.service.ptProcess.SpecificConsignmentSetService;
import org.tnt.pt.service.ptProcess.SpecificCountryService;
import org.tnt.pt.service.ptProcess.ZoneSummaryService;
import org.tnt.pt.util.DoubleUtil;
import org.tnt.pt.util.PTPARAMETERS;
import org.tnt.pt.vo.BaseVO;
import org.tnt.pt.vo.BusinessVO;
import org.tnt.pt.vo.HWRateVO;
import org.tnt.pt.vo.RevVO;

/**
 * ProductController负责产品的请求，
 * 
 * @author yuanchen
 */
@Controller
@RequestMapping(value = "/ptQuery")
public class PTQueryController{
    @Autowired
	BusinessService businessService;
    @Autowired
	ZoneGroupService zonegroupService;
    @Autowired
	ZoneTypeService zoneTypeService;
    @Autowired
	WeightBandService weightBandService;
    @Autowired
	CustomerService customerService;
    @Autowired
	ProductService productService;
    @Autowired
	DiscountService discountService;
    @Autowired
	TariffService tariffService;
    @Autowired
	RateService rateService;
    @Autowired
   	GeoSummaryService geoSummaryService;
    @Autowired
    ZoneSummaryService zoneSummaryService;
    @Autowired
    CountryGeoService countryGeoService;
    @Autowired
    CountryZoneService countryZoneService;
    @Autowired
    ConsignmentService consignmentService;
    @Autowired
    SpecificConsignmentSetService specificConsignmentSetService;
    @Autowired
    SpecificCountryService specificCountryService; 
    @Autowired
    RevService revService;
    @Autowired
    ExamService examService;
    @Autowired
    HWRateService hwRateService;
    @Autowired
    BsmRgmService bsmRgmService;
    @Autowired
    AccountService accountService;
    @Autowired
    TariffGroupService tariffGroupService;
	
    /**
	 * 公斤_时区_折扣  详细页 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tariffPT/{id}", method = RequestMethod.GET)
	public String rateDetail(Model model,@PathVariable("id") Long id) {
		/**
		 * 该处为保存该pt下折扣信息代码
		 */
		//List<WeightBand> weightBandList = new ArrayList<WeightBand>();
		List<Tariff> tariffList = new ArrayList<Tariff>();
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> economyList = new ArrayList<WeightBand>();
		List<Discount> discountList = new ArrayList<Discount>();
		List<Discount> recDiscountList = new ArrayList<Discount>();
		List<Consignment>  consignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<Consignment>  recConsignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		Map<String,Double> rateMap = new HashMap<String,Double>();//形成价格map 方便查询
		Map<String,Double> recRateMap = new HashMap<String,Double>();//形成价格map 方便查询
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		//cons 取件数
		Map<String,Integer> consignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Integer> recConsignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		ZoneSummary zoneSummary = new ZoneSummary();
		ZoneSummary recZoneSummary = new ZoneSummary();
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		String flag = "";//标识是否需要两套数据展示
		business = businessService.getBusiness(id);//1L为保存后获得的PT业务主表id
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		customer = customerService.getCustomer(business.getCustomerId());//客户信息
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(zoneType.getZoneType());
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		economyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		flag = customer.getPayment();
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[0]);
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[1]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[0]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[1]);
			for (Consignment consignment:recConsignmentList) {
				recConsignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[0]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
			/**
			 * 获取汇总信息
			 */
			revVO = null;
			revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[1]).get(0);
			recZoneSummary.setConsM(revVO.getCons());
			recZoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			recZoneSummary.setKiloM(revVO.getKilo());
			recZoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			recZoneSummary.setRevM(revVO.getRev());
			recZoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);//都取both
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[2]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[2]);
			for (Consignment consignment:recConsignmentList) {
				recConsignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[2]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}else{
			discountList = discountService.getAllDiscountByBusId(id,customer.getPayment());
			consignmentList = consignmentService.getAllConsignmentByBusId(id,customer.getPayment());
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,customer.getPayment()).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}
		for (Discount discount:discountList) {
			discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
		}
		for (Consignment consignment:consignmentList) {
			consignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
		}
		tariffList = tariffService.getAllTariff();
		for (Tariff tariff:tariffList) {
			String keyId = tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId();
			if(discountMap.get(keyId)!=null){
				rateMap.put(keyId, tariff.getTariff()*discountMap.get(keyId)/100);
			}
			if(recDiscountMap.size()>1){
				if(recDiscountMap.get(keyId)!=null){
					recRateMap.put(keyId, tariff.getTariff()*recDiscountMap.get(keyId)/100);
				}
			}
		}
		model.addAttribute("flag",flag);
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("zoneType", zoneType);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("documentList", documentList);
		model.addAttribute("ndocumentList", ndocumentList);
		model.addAttribute("economyList", economyList);
		model.addAttribute("rateMap", rateMap);
		model.addAttribute("recRateMap", recRateMap);
		model.addAttribute("discountMap", discountMap);
		model.addAttribute("recDiscountMap", recDiscountMap);
		model.addAttribute("consignmentMap", consignmentMap);
		model.addAttribute("recConsignmentMap", recConsignmentMap);
		model.addAttribute("zoneSummary", zoneSummary);
		model.addAttribute("recZoneSummary", recZoneSummary);
		
		/*
		 * HW 记录 开始
		 */
		Map<String,Double> hwRateMap = new HashMap<String,Double>();//形成折扣map 方便查询
		Map<String,Double> recHwRateMap = new HashMap<String,Double>();//形成折扣map 方便查询
		List<Country> ndocountrys = new ArrayList<Country>();
		List<Country> ecocountrys = new ArrayList<Country>();
		List<WeightBand> ndocumentList_ = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList_ = new ArrayList<WeightBand>();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		List<HWRate> recHwRateList = new ArrayList<HWRate>();
		ndocumentList_ = weightBandService.getAllHighWeightBandByProductId(zoneType.getNonDocument());//获取重货
		eonomyList_ = weightBandService.getAllHighWeightBandByProductId(zoneType.getEconomy());//获取重货
		
		ndocountrys = hwRateService.getCountry(business.getId(), zoneType.getNonDocument());
		ecocountrys = hwRateService.getCountry(business.getId(), zoneType.getEconomy());

		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[0]);
			recHwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			for (HWRate hwRate:recHwRateList) {
				recHwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
			}
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recHwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			for (HWRate hwRate:recHwRateList) {
				recHwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
			}
		}else{
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),customer.getPayment());
		}
		for (HWRate hwRate:hwRateList) {
			hwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
		}
		/*
		 * HW 记录 结束
		 */
		model.addAttribute("ndocumentCountrys", ndocountrys);
		model.addAttribute("eonomyCountrys", ecocountrys);
		model.addAttribute("hwRateMap", hwRateMap);
		model.addAttribute("recHwRateMap", recHwRateMap);
		model.addAttribute("ndocumentList_", ndocumentList_);
		model.addAttribute("eonomyList_", eonomyList_);
		model.addAttribute("eonomy", zoneType.getEconomy());
		model.addAttribute("ndocument", zoneType.getNonDocument());
		
		//查看该pt是否有导出价卡等功能
		String exportFlag = "";
		if(business.getState().equals(PTPARAMETERS.PROCESS_SATE[3])||
				business.getState().equals(PTPARAMETERS.PROCESS_SATE[4])||
				business.getState().equals(PTPARAMETERS.PROCESS_SATE[5])){
			exportFlag = "yes";
		}else{
			exportFlag = "no";
		}
		model.addAttribute("exportFlag",exportFlag);
		return "ptProcess/tariffPT";
	}
	
	/**
	 * copy 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="copy", method = RequestMethod.GET)
	public String copy(Model model) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model", businessVO);
		return "query/copyPTQuery";
	}
	
	/**
	 * copy 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="copyQuery", method = RequestMethod.POST)
	public String copyQuery(Model model,@ModelAttribute BusinessVO businessVO) {
		List<Business> businessList = new ArrayList<Business>();
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model", businessVO);
		return "query/copyPTQuery";
	}
	
	/**
	 * PT QUERY 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="queryPTInit", method = RequestMethod.GET)
	public String ptQueryInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		Calendar calendar = Calendar.getInstance();  
		calendar.add(Calendar.MONTH, -3); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		businessVO.setStartDate(sdf.format(calendar.getTime()));
		//获得当前用户所属depot 可能是多个
		User user = (User) request.getSession().getAttribute("user");
		List<String> statusList = new ArrayList<String>();
		List<String> depotList = new ArrayList<String>();
		statusList.add("");
		for(String status:PTPARAMETERS.PROCESS_SATE){
			statusList.add(status);
		}
		
		if(user!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[1])){//区域经理则展示区域内depot
			depotList = bsmRgmService.getDepotByName(user.getUserName());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[0])||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[4]))){//sales 或者bsm 则显示对应depot
			depotList.add(user.getCode());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[2])||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[3]))||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[5])){//显示所有depot
			depotList = accountService.getAllDepots(user.getUserName());
		}
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("depotList", depotList);
		model.addAttribute("statusList",statusList);
		businessVO.setStartDate(null);
		model.addAttribute("model", businessVO);
		return "query/queryPT";
	}
	
	
	/**
	 * PT QUERY
	 * @param model
	 * @return
	 */
	@RequestMapping(value="queryPT", method = RequestMethod.POST)
	public String ptQuery(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		List<String> depotList = new ArrayList<String>();
		User user = (User) request.getSession().getAttribute("user");
		List<String> statusList = new ArrayList<String>();
		statusList.add("");
		for(String status:PTPARAMETERS.PROCESS_SATE){
			statusList.add(status);
		}
		if(user!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[1])){//区域经理则展示区域内depot
			depotList = bsmRgmService.getDepotByName(user.getUserName());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[0])||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[4]))){//sales 或者bsm 则显示对应depot
			depotList.add(user.getCode());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[2])||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[3]))||
				user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[5])){//显示所有depot
			depotList = accountService.getAllDepots(user.getUserName());
		}
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("depotList", depotList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("model",businessVO);
		return "query/queryPT";
	}
	/**
	 * PT QUERY 发送邮件
	 * @param model
	 * @return
	 */
	@RequestMapping(value="appeal", method = RequestMethod.POST)
	public String appeal(Model model,@ModelAttribute BusinessVO businessVO) {
		List<Business> businessList = new ArrayList<Business>();
		EmailSend cn = new EmailSend();
		// 设置发件人地址、收件人地址和邮件标题    获得commercial的邮箱即可
		cn.send("the applicationReference of "+businessVO.getApplicationReference()+"is waiting for approve!","24136471@qq.com");
		businessList = businessService.getBusinessByBusiness(new BusinessVO());
		businessVO.setApplicationReference("");
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "query/queryPT";
	}
	
	/**
	 * PT MODIFY 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptModifyInit", method = RequestMethod.GET)
	public String ptModifyInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		User user = (User) request.getSession().getAttribute("user");
		businessVO.setUserName(user.getUserName());businessVO.setDepot(user.getCode());
		businessList = examService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model", businessVO);
		return "query/modifyPT";
	}
	
	/**
	 * PT MODIFY
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptModify", method = RequestMethod.POST)
	public String ptModify(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		User user = (User) request.getSession().getAttribute("user");
		businessVO.setUserName(user.getUserName());
		businessList = examService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "query/modifyPT";
	}
	
	/**
	 * PT MODIFY 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="modifyCommercial", method = RequestMethod.POST)
	public String modifyInitCommercial(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		User user = (User) request.getSession().getAttribute("user");
		List<String> depotList = new ArrayList<String>();
		depotList = accountService.getAllDepots(user.getUserName());
		businessVO.setState(PTPARAMETERS.PROCESS_SATE[2]);
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model", businessVO);
		model.addAttribute("depotList", depotList);
		return "query/modifyCommercial";
	}
	
	/**
	 * PT MODIFY 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="modifyCommercialInit", method = RequestMethod.GET)
	public String modifyCommercialInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		User user = (User) request.getSession().getAttribute("user");
		List<String> depotList = new ArrayList<String>();
		depotList = accountService.getAllDepots(user.getUserName());
		businessVO.setState(PTPARAMETERS.PROCESS_SATE[2]);
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model", businessVO);
		model.addAttribute("depotList", depotList);
		return "query/modifyCommercial";
	}
	
	/**
	 * PT delete
	 * @param model
	 * @return
	 */
	@RequestMapping(value="deletePT", method = RequestMethod.POST)
	public String deletePT(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		Long businessId = Long.parseLong(businessVO.getId());
		businessService.deleteBusiness(businessId);
		List<Business> businessList = new ArrayList<Business>();
		User user = (User) request.getSession().getAttribute("user");
		businessVO.setUserName(user.getUserName());
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "query/modifyPT";
	}
	
	/**
	 * 修改页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="updateCustomer/{id}", method = RequestMethod.POST)
	public String updateCustomer(Model model,@PathVariable("id") Long businessId) {
		Business business = businessService.getBusiness(businessId);
		Customer cus = customerService.getCustomer(business.getCustomerId());
		model.addAttribute("customer", cus);
		model.addAttribute("business", business);
		return "newPT/copyPTCustomer";
	}
	
	/**
	 * PT QUERY 初始 历史数据 默认三个月前的
	 * @param model
	 * @return
	 */
	@RequestMapping(value="hisPTQueryInit", method = RequestMethod.GET)
	public String hisPTQueryInit(Model model) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		Calendar calendar = Calendar.getInstance();  
		calendar.add(Calendar.MONTH, -3); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		businessVO.setEndDate(sdf.format(calendar.getTime()));
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "query/hisPTQuery";
	}
	
	
	/**
	 * PT QUERY
	 * @param model
	 * @return
	 */
	@RequestMapping(value="hisPTQuery", method = RequestMethod.POST)
	public String hisPTQuery(Model model,@ModelAttribute BusinessVO businessVO) {
		List<Business> businessList = new ArrayList<Business>();
		Calendar calendar = Calendar.getInstance();  
		calendar.add(Calendar.MONTH, -3); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		businessVO.setEndDate(sdf.format(calendar.getTime()));
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "query/hisPTQuery";
	}
	/**
	 * PT Approve
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptApproveInit", method = RequestMethod.GET)
	public String ptApproveInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		//获得当前用户所属depot 可能是多个
		BusinessVO businessVO = new BusinessVO();
		User user = (User) request.getSession().getAttribute("user");
		List<String> depotList = new ArrayList<String>();
		if(user!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[1])){//区域经理则展示区域内depot
			depotList = bsmRgmService.getDepotByName(user.getUserName());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[0]))){//bsm 则显示对应depot
			depotList.add(user.getCode());
		}
		businessList = businessService.getBusinessByUser(user,businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		model.addAttribute("depotList", depotList);
		return "ptProcess/approvePT";
	}
	
	/**
	 * PT Approve
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptApprove", method = RequestMethod.POST)
	public String ptApprove(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		//获得当前用户所属depot 可能是多个
		User user = (User) request.getSession().getAttribute("user");
		List<String> depotList = new ArrayList<String>();
		if(user!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[1])){//区域经理则展示区域内depot
			depotList = bsmRgmService.getDepotByName(user.getUserName());
		}
		if(user!=null&&(user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[0]))){//bsm 则显示对应depot
			depotList.add(user.getCode());
		}
		businessList = businessService.getBusinessByUser(user,businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		model.addAttribute("depotList", depotList);
		return "ptProcess/approvePT";
	}
	
	/**
	 * PT PTSLoad
	 * @param model
	 * @return
	 */
	@RequestMapping(value="PTSLoadInit", method = RequestMethod.GET)
	public String PTSLoadInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		User user = (User) request.getSession().getAttribute("user");
		businessList = businessService.getBusinessByUser(user,businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/PTSLoad";
	}
	
	/**
	 * PT PTSLoad
	 * @param model
	 * @return
	 */
	@RequestMapping(value="PTSLoad", method = RequestMethod.POST)
	public String PTSLoad(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		User user = (User) request.getSession().getAttribute("user");
		businessList = businessService.getBusinessByUser(user,businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/PTSLoad";
	}
	/**
	 * PT confirmRate 初始化
	 * @param model
	 * @return
	 */
	@RequestMapping(value="confirmRateInit", method = RequestMethod.GET)
	public String confirmRateInit(Model model,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		User user = (User) request.getSession().getAttribute("user");
		businessVO.setUserName(user.getUserName());
		businessVO.setState(PTPARAMETERS.PROCESS_SATE[3]);
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/confirmRate";
	}
	
	
	/**
	 * PT confirmRate
	 * @param model
	 * @return
	 */
	@RequestMapping(value="confirmRate", method = RequestMethod.POST)
	public String confirmRate(Model model,@ModelAttribute BusinessVO businessVO,HttpServletRequest request) {
		List<Business> businessList = new ArrayList<Business>();
		businessVO.setState(PTPARAMETERS.PROCESS_SATE[3]);
		User user = (User) request.getSession().getAttribute("user");
		businessVO.setUserName(user.getUserName());
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/confirmRate";
	}
	
	/**
	 * PT confirmToBilling 初始化 该节点即上面
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value="confirmToBillingInit", method = RequestMethod.GET)
	public String confirmToBillingInit(Model model) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/confirmToBilling";
	}
	
	
	*//**
	 * PT confirmToBilling
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value="confirmToBilling", method = RequestMethod.POST)
	public String confirmToBilling(Model model,@ModelAttribute BusinessVO businessVO) {
		List<Business> businessList = new ArrayList<Business>();
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/confirmToBilling";
	}*/
	
	/**
	 * PT Analyse a PT
	 * @param model
	 * @return
	 */
	@RequestMapping(value="analysePT", method = RequestMethod.GET)
	public String analysePT(Model model) {
		String state = PTPARAMETERS.PROCESS_SATE[2];
		List<PTSLoad> loadList = businessService.getAnalysePT(state);
		model.addAttribute("loadList", loadList);
		return "ptProcess/analysePT";
	}
	
	/**
	 * PT confirmToBilling
	 * @param model
	 * @return
	 */
	@RequestMapping(value="analyseDetailPool/{depotCode}", method = RequestMethod.GET)
	public String analyseDetailPool(Model model,@PathVariable("depotCode") String depotCode) {
		List<Business> businessList = new ArrayList<Business>();
		BusinessVO businessVO = new BusinessVO();
		businessVO.setState(PTPARAMETERS.PROCESS_SATE[2]);
		businessVO.setDepot(depotCode);
		businessList = businessService.getBusinessByBusiness(businessVO);
		model.addAttribute("businessList", businessList);
		model.addAttribute("model",businessVO);
		return "ptProcess/analyseDetailPool";
	}
	
	/**
	 * PT confirmToBilling
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getZoneTableInit", method = RequestMethod.GET)
	public String getZoneTableInit(Model model) {
		List<ZoneTable> zoneTableList = new ArrayList<ZoneTable>();
		List<ZoneType> zoneTypeList = new ArrayList<ZoneType>();
		zoneTypeList =  zoneTypeService.getAllZoneType();
		Map hashMap = new HashMap();
		hashMap.put("baseVO", new BaseVO());
		if(zoneTypeList.size()>0){
			ZoneType zoneType = zoneTypeList.get(0);
			hashMap.put("type", zoneType.getZoneType());
			zoneTableList = zonegroupService.getZoneTable(hashMap);
		}
		model.addAttribute("zoneTableList", zoneTableList);
		model.addAttribute("zoneTypeList", zoneTypeList);
		model.addAttribute("model",hashMap.get("baseVO"));
		return "ptProcess/zoneTable";
	}
	
	
	/**
	 * PT confirmToBilling
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getZoneTable", method = RequestMethod.POST)
	public String getZoneTable(Model model,@RequestParam(value = "id", required = false) Long id,BaseVO baseVO) {
		ZoneType zoneType = zoneTypeService.getZoneTypeById(id);
		List<ZoneTable> zoneTableList = new ArrayList<ZoneTable>();
		List<ZoneType> zoneTypeList = new ArrayList<ZoneType>();
		zoneTypeList =  zoneTypeService.getAllZoneType();
		Map hashMap = new HashMap();
		hashMap.put("type", zoneType.getZoneType());
		hashMap.put("baseVO", baseVO);
		zoneTableList = zonegroupService.getZoneTable(hashMap);
		model.addAttribute("zoneTableList", zoneTableList);
		model.addAttribute("zoneTypeList", zoneTypeList);
		model.addAttribute("model",hashMap.get("baseVO"));
		return "ptProcess/zoneTable";
	}
	
	/**
	 * 导出计算结果
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/exportBatchExcel", method = RequestMethod.POST)
	public String exportBatchExcel(Model model,@ModelAttribute BusinessVO businessVO,HttpServletResponse response) {
		HSSFWorkbook workbook = null;
		try {
			List<Business> businessList = new ArrayList<Business>();
			businessList = examService.getBusiness(businessVO);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			workbook = new HSSFWorkbook();//创建工作薄
			DecimalFormat df = new DecimalFormat("0.##");
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(0, "PT LIST");
			String headers[] = {"PT Application Reference","Application Date","Account","Customer Name","Depot","Channel","Top","Territory","Status"};
			HSSFRow row = sheet.createRow((short) 0);//第一行 表头
			for(short i = 0;i < headers.length;i++){
				HSSFCell cell = row.createCell(i);      //创建第1行单元格   
			    cell.setCellValue(headers[i]);
			}
			//第二行开始是数据
	        for(int i=1;i<businessList.size()+1;i++){
	        	HSSFRow rowdata = sheet.createRow((short) i);
	        	Business bs = businessList.get(i-1);
	        	HashMap<Integer, String> cellMap = new HashMap<Integer, String>();
	        	cellMap.put(0, bs.getApplicationReference());
	        	cellMap.put(1, sdf.format(bs.getApplicationDate()));
	        	cellMap.put(2, bs.getAccount());
	        	cellMap.put(3, bs.getCusName());cellMap.put(4, bs.getDepotCode());
	        	cellMap.put(5, bs.getChannel());cellMap.put(6, bs.getConsStop().toString());
	        	cellMap.put(7, bs.getTerritory());cellMap.put(8, bs.getState());
	        	for (int j = 0; j < headers.length; j++) {
	    			HSSFCell celldata = rowdata.createCell((short) j);      // 在上面行索引0的位置创建单元格
	    			celldata.setCellType(Cell.CELL_TYPE_STRING);     	// 定义单元格为字符串类型
	    			celldata.setCellValue(cellMap.get(j)+ "");
				}
	        }
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			// 表示以附件的形式把文件发送到客户端
			response.setHeader("Content-Disposition", "attachment;filename="
						+ new String(((new Date().getTime())+".xls").getBytes(), "ISO8859-1"));
			// 通过response的输出流把工作薄的流发送浏览器形成文件
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return "query/queryPT";
	}
	
	/**
	 * summaryInfo 详细页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="summaryInfoProcess/{id}", method = RequestMethod.GET)
	public String summaryInfoProcess(Model model,@PathVariable("id") Long id) {
		
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		List<ZoneSummary> zoneSummaryList = new ArrayList<ZoneSummary>();
		List<GEOSummary> geoSummaryList = new ArrayList<GEOSummary>();
		List<ZoneSummary> recZoneSummaryList = new ArrayList<ZoneSummary>();
		List<GEOSummary> recGeoSummaryList = new ArrayList<GEOSummary>();
		List<Discount> discountList = new ArrayList<Discount>();
		List<Discount> recDiscountList = new ArrayList<Discount>();
		//cons 取件数
		Map<String,Integer> consignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Integer> recConsignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		List<Consignment>  consignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<Consignment>  recConsignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<RevVO> revList = new ArrayList<RevVO>();
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		ZoneSummary zoneSummary = new ZoneSummary();
		ZoneSummary recZoneSummary = new ZoneSummary();
		String[] groupBy = {"zoneGroupId"};
		business = businessService.getBusiness(id);
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
		
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		eonomyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		
		String flag = "";//标识是否需要两套数据展示
		flag = customer.getPayment();
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[0]);
			recZoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			recGeoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[0]);
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[1]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[0]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[1]);
			for (Consignment consignment:recConsignmentList) {
				recConsignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
			}
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[0]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[0]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
			revList = null;
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[1]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				recZoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			revVO = null;
			revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[1]).get(0);
			recZoneSummary.setConsM(revVO.getCons());
			recZoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			recZoneSummary.setKiloM(revVO.getKilo());
			recZoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			recZoneSummary.setRevM(revVO.getRev());
			recZoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
			
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recZoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recGeoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);//都取both
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[2]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(id,PTPARAMETERS.PAYMENT[2]);
			for (Consignment consignment:recConsignmentList) {
				recConsignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
			}
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[2]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[2]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}else{
			zoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),customer.getPayment());
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),customer.getPayment());
			discountList = discountService.getAllDiscountByBusId(id,customer.getPayment());
			consignmentList = consignmentService.getAllConsignmentByBusId(id,customer.getPayment());
			revList = revService.getGroupBy(business.getId(),groupBy,customer.getPayment());
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,customer.getPayment()).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}
		for (Consignment consignment:consignmentList) {
			consignmentMap.put(consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
		}
		for (Discount discount:discountList) {
			discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
		}
		model.addAttribute("flag", flag);
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("geoSummaryList", geoSummaryList);
		model.addAttribute("zoneSummaryList", zoneSummaryList);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("recZoneSummaryList", recZoneSummaryList);
		model.addAttribute("recGeoSummaryList", recGeoSummaryList);
		model.addAttribute("documentList", documentList);
		model.addAttribute("ndocumentList", ndocumentList);
		model.addAttribute("eonomyList", eonomyList);
		model.addAttribute("zoneSummary", zoneSummary);
		model.addAttribute("recZoneSummary", recZoneSummary);
		model.addAttribute("economyList", eonomyList);
		model.addAttribute("consignmentMap", consignmentMap);
		model.addAttribute("recConsignmentMap", recConsignmentMap);
		model.addAttribute("recDiscountMap", recDiscountMap);
		model.addAttribute("discountMap", discountMap);
		
		/*
		 * HW 记录 开始
		 */
		Map<String,Double> hwRateMap = new HashMap<String,Double>();//形成折扣map 方便查询
		Map<String,Double> recHwRateMap = new HashMap<String,Double>();//形成折扣map 方便查询
		List<Country> ndocountrys = new ArrayList<Country>();
		List<Country> ecocountrys = new ArrayList<Country>();
		List<WeightBand> ndocumentList_ = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList_ = new ArrayList<WeightBand>();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		List<HWRate> recHwRateList = new ArrayList<HWRate>();
		ndocumentList_ = weightBandService.getAllHighWeightBandByProductId(zoneType.getNonDocument());//获取重货
		eonomyList_ = weightBandService.getAllHighWeightBandByProductId(zoneType.getEconomy());//获取重货
		
		ndocountrys = hwRateService.getCountry(business.getId(), zoneType.getNonDocument());
		ecocountrys = hwRateService.getCountry(business.getId(), zoneType.getEconomy());

		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[0]);
			recHwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			for (HWRate hwRate:recHwRateList) {
				recHwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
			}
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recHwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			for (HWRate hwRate:recHwRateList) {
				recHwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
			}
		}else{
			hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),customer.getPayment());
		}
		for (HWRate hwRate:hwRateList) {
			hwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
		}
		/*
		 * HW 记录 结束
		 */
		model.addAttribute("ndocumentCountrys", ndocountrys);
		model.addAttribute("eonomyCountrys", ecocountrys);
		model.addAttribute("hwRateMap", hwRateMap);
		model.addAttribute("recHwRateMap", recHwRateMap);
		model.addAttribute("ndocumentList_", ndocumentList_);
		model.addAttribute("eonomyList_", eonomyList_);
		model.addAttribute("eonomy", zoneType.getEconomy());
		model.addAttribute("ndocument", zoneType.getNonDocument());
		return "ptProcess/summaryInfoProgress";
	}
	
	/**
	 * summaryInfo 详细页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="summaryInfo/{id}", method = RequestMethod.GET)
	public String summaryInfo(Model model,@PathVariable("id") Long id) {
		
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		List<ZoneSummary> zoneSummaryList = new ArrayList<ZoneSummary>();
		List<GEOSummary> geoSummaryList = new ArrayList<GEOSummary>();
		List<ZoneSummary> recZoneSummaryList = new ArrayList<ZoneSummary>();
		List<GEOSummary> recGeoSummaryList = new ArrayList<GEOSummary>();
		List<Discount> discountList = new ArrayList<Discount>();
		List<Discount> recDiscountList = new ArrayList<Discount>();

		List<RevVO> revList = new ArrayList<RevVO>();
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		ZoneSummary zoneSummary = new ZoneSummary();
		ZoneSummary recZoneSummary = new ZoneSummary();
		String[] groupBy = {"zoneGroupId"};
		business = businessService.getBusiness(id);
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
		
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		eonomyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		
		String flag = "";//标识是否需要两套数据展示
		flag = customer.getPayment();
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[0]);
			recZoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			recGeoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[1]);
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[0]);
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[1]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[0]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[0]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
			revList = null;
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[1]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				recZoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			revVO = null;
			revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[1]).get(0);
			recZoneSummary.setConsM(revVO.getCons());
			recZoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			recZoneSummary.setKiloM(revVO.getKilo());
			recZoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			recZoneSummary.setRevM(revVO.getRev());
			recZoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
			
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recZoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			recGeoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			discountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);//都取both
			recDiscountList = discountService.getAllDiscountByBusId(id,PTPARAMETERS.PAYMENT[2]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			revList = revService.getGroupBy(business.getId(),groupBy,PTPARAMETERS.PAYMENT[2]);
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,PTPARAMETERS.PAYMENT[2]).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}else{
			zoneSummaryList = zoneSummaryService.getAllZoneSummaryByBusinessId(business.getId(),customer.getPayment());
			geoSummaryList = geoSummaryService.getAllGeoSummaryByBusinessId(business.getId(),customer.getPayment());
			discountList = discountService.getAllDiscountByBusId(id,customer.getPayment());
			revList = revService.getGroupBy(business.getId(),groupBy,customer.getPayment());
			for (RevVO rev : revList) {
				ZoneSummary zs = new ZoneSummary();
				zs.setConsM(rev.getCons());
				zs.setConsY(DoubleUtil.get2Double(rev.getCons()*12));
				zs.setKiloM(rev.getKilo());
				zs.setKiloY(DoubleUtil.get2Double(rev.getKilo()*12));
				zs.setRevM(rev.getRev());
				zs.setRevY(DoubleUtil.get2Double(rev.getRev()*12));
				zs.setZoneType(rev.getZone());
				zoneSummaryList.add(zs);
			}
			/**
			 * 获取汇总信息
			 */
			String[] groupBy_1 = {};
			RevVO revVO = revService.getGroupBy(business.getId(),groupBy_1,customer.getPayment()).get(0);
			zoneSummary.setConsM(revVO.getCons());
			zoneSummary.setConsY(DoubleUtil.get2Double(revVO.getCons()*12));
			zoneSummary.setKiloM(revVO.getKilo());
			zoneSummary.setKiloY(DoubleUtil.get2Double(revVO.getKilo()*12));
			zoneSummary.setRevM(revVO.getRev());
			zoneSummary.setRevY(DoubleUtil.get2Double(revVO.getRev()*12));
		}
		for (Discount discount:discountList) {
			discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
		}
		model.addAttribute("flag", flag);
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("geoSummaryList", geoSummaryList);
		model.addAttribute("zoneSummaryList", zoneSummaryList);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("recZoneSummaryList", recZoneSummaryList);
		model.addAttribute("recGeoSummaryList", recGeoSummaryList);
		model.addAttribute("documentList", documentList);
		model.addAttribute("ndocumentList", ndocumentList);
		model.addAttribute("eonomyList", eonomyList);
		model.addAttribute("zoneSummary", zoneSummary);
		model.addAttribute("recZoneSummary", recZoneSummary);
		model.addAttribute("economyList", eonomyList);
		model.addAttribute("recDiscountMap", recDiscountMap);
		model.addAttribute("discountMap", discountMap);
		return "ptProcess/summaryInfo";
	}
	
	/**
	 * 公斤_时区_折扣  详细页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="rates/{businessId}", method = RequestMethod.GET)
	public String rates(Model model,@PathVariable("businessId") Long businessId) {
		String flag = "";
		/**
		 * 该处为保存该pt下折扣信息代码
		 */
		List<ZoneType> zoneTypeList = new ArrayList<ZoneType>();
		/**
		 * 假如zonetype不为空，则默认初始加载的为第一个zonetype
		 */
		zoneTypeList =  zoneTypeService.getAllZoneType();
		Map<String,Double> traiffMapSP = new HashMap<String,Double>();//形成折扣map 方便查询
		Map<String,Double> traiffMapRP = new HashMap<String,Double>();//形成折扣map 方便查询
		
		List<Tariff> documentListSP = new ArrayList<Tariff>();
		List<Tariff> ndocumentListSP = new ArrayList<Tariff>();
		List<Tariff> eonomyListSP = new ArrayList<Tariff>();
		List<Tariff> documentListRP = new ArrayList<Tariff>();
		List<Tariff> ndocumentListRP = new ArrayList<Tariff>();
		List<Tariff> eonomyListRP = new ArrayList<Tariff>();
		
		List<TariffGroup> GdocumentListSP = new ArrayList<TariffGroup>();
		List<TariffGroup> GndocumentListSP = new ArrayList<TariffGroup>();
		List<TariffGroup> GeonomyListSP = new ArrayList<TariffGroup>();
		List<TariffGroup> GdocumentListRP = new ArrayList<TariffGroup>();
		List<TariffGroup> GndocumentListRP = new ArrayList<TariffGroup>();
		List<TariffGroup> GeonomyListRP = new ArrayList<TariffGroup>();
		
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		Business business = businessService.getBusiness(businessId);
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
		
		documentListSP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[0], zoneType.getDocument());
		ndocumentListSP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[0], zoneType.getNonDocument());
		eonomyListSP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[0], zoneType.getEconomy());
		documentListRP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[1], zoneType.getDocument());
		ndocumentListRP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[1], zoneType.getNonDocument());
		eonomyListRP = examService.getTariff(businessId, PTPARAMETERS.PAYMENT[1], zoneType.getEconomy());
		
		GdocumentListSP = tariffGroupService.getAllTariffGroup(zoneType.getDocument(),PTPARAMETERS.PAYMENT[0]);
		GndocumentListSP = tariffGroupService.getAllTariffGroup(zoneType.getNonDocument(),PTPARAMETERS.PAYMENT[0]);
		GeonomyListSP = tariffGroupService.getAllTariffGroup(zoneType.getEconomy(),PTPARAMETERS.PAYMENT[0]);
		GdocumentListRP = tariffGroupService.getAllTariffGroup(zoneType.getDocument(),PTPARAMETERS.PAYMENT[1]);
		GndocumentListRP = tariffGroupService.getAllTariffGroup(zoneType.getNonDocument(),PTPARAMETERS.PAYMENT[1]);
		GeonomyListRP = tariffGroupService.getAllTariffGroup(zoneType.getEconomy(),PTPARAMETERS.PAYMENT[1]);
		flag = customer.getPayment();
		for (Tariff tariff:documentListSP) {
			traiffMapSP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		for (Tariff tariff:ndocumentListSP) {
			traiffMapSP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		for (Tariff tariff:eonomyListSP) {
			traiffMapSP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		for (Tariff tariff:documentListRP) {
			traiffMapRP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		for (Tariff tariff:ndocumentListRP) {
			traiffMapRP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		for (Tariff tariff:eonomyListRP) {
			traiffMapRP.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
		}
		
		model.addAttribute("flag", flag);
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("documentTGList", GdocumentListSP);
		model.addAttribute("ndocumentTGList", GndocumentListSP);
		model.addAttribute("economyTGList", GeonomyListSP);
		model.addAttribute("documentTGListR", GdocumentListRP);
		model.addAttribute("ndocumentTGListR", GndocumentListRP);
		model.addAttribute("economyTGListR", GeonomyListRP);
		model.addAttribute("traiffMapRP", traiffMapRP);
		model.addAttribute("traiffMapSP", traiffMapSP);
		
		return "ptProcess/rateInfo";
	}
	
	/**
	 * 公斤_时区_折扣  详细页 新增
	 * @param model
	 * @return 
	 */
	@RequestMapping(value="stateLog/{id}", method = RequestMethod.GET)
	public String stateLog(Model model,@PathVariable("id") Long id) {
		List<Exam> statusList = new ArrayList<Exam>(); 
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map map = new HashMap();
		map.put("businessId", id);
//		Business business = businessService.getBusiness(id);
		statusList = examService.getStatusLog(map);
		for(Exam exam:statusList){
			exam.setShowTime(sdfDateFormat.format(exam.getExamTime()));
		}
		model.addAttribute("statusList",statusList);
		return "query/statusLog";
	}
}
