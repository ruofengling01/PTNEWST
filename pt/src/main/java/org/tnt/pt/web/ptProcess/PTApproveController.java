package org.tnt.pt.web.ptProcess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.mapper.JsonMapper;
import org.tnt.pt.dmsentity.User;
import org.tnt.pt.entity.Business;
import org.tnt.pt.entity.BusinessFile;
import org.tnt.pt.entity.Consignment;
import org.tnt.pt.entity.Country;
import org.tnt.pt.entity.CountryZone;
import org.tnt.pt.entity.Customer;
import org.tnt.pt.entity.CustomerLog;
import org.tnt.pt.entity.Discount;
import org.tnt.pt.entity.DiscountDefault;
import org.tnt.pt.entity.Exam;
import org.tnt.pt.entity.HWRate;
import org.tnt.pt.entity.Product;
import org.tnt.pt.entity.Rev;
import org.tnt.pt.entity.SpecificConsignmentSet;
import org.tnt.pt.entity.Tariff;
import org.tnt.pt.entity.TariffGroup;
import org.tnt.pt.entity.WeightBand;
import org.tnt.pt.entity.ZoneGroup;
import org.tnt.pt.entity.ZoneSummary;
import org.tnt.pt.entity.ZoneType;
import org.tnt.pt.service.account.AccountService;
import org.tnt.pt.service.baseInfo.CountryGeoService;
import org.tnt.pt.service.baseInfo.CountryService;
import org.tnt.pt.service.baseInfo.CountryZoneService;
import org.tnt.pt.service.baseInfo.DiscountDefaultService;
import org.tnt.pt.service.baseInfo.ProductService;
import org.tnt.pt.service.baseInfo.TariffGroupService;
import org.tnt.pt.service.baseInfo.TariffService;
import org.tnt.pt.service.baseInfo.WeightBandService;
import org.tnt.pt.service.baseInfo.ZoneGroupService;
import org.tnt.pt.service.baseInfo.ZoneTypeService;
import org.tnt.pt.service.costcalculator.CostCalculator;
import org.tnt.pt.service.costcalculator.CostCalculator_Service;
import org.tnt.pt.service.dms.FsiService;
import org.tnt.pt.service.ptProcess.BusinessService;
import org.tnt.pt.service.ptProcess.ConsignmentService;
import org.tnt.pt.service.ptProcess.CustomerService;
import org.tnt.pt.service.ptProcess.DiscountService;
import org.tnt.pt.service.ptProcess.ExamService;
import org.tnt.pt.service.ptProcess.GeoSummaryService;
import org.tnt.pt.service.ptProcess.HWRateService;
import org.tnt.pt.service.ptProcess.PtApproveService;
import org.tnt.pt.service.ptProcess.RateService;
import org.tnt.pt.service.ptProcess.RevService;
import org.tnt.pt.service.ptProcess.SpecificConsignmentSetService;
import org.tnt.pt.service.ptProcess.SpecificCountryService;
import org.tnt.pt.service.ptProcess.ZoneSummaryService;
import org.tnt.pt.util.DoubleUtil;
import org.tnt.pt.util.FileOperateUtil;
import org.tnt.pt.util.FileUtil;
import org.tnt.pt.util.PTPARAMETERS;
import org.tnt.pt.vo.BusinessVO;
import org.tnt.pt.vo.JsonData;
import org.tnt.pt.vo.RevVO;
import org.tnt.pt.web.ptProcess.PTCreateController.ComparatorWeightBand;

/**
 * PT审批流程controller
 * 
 * @author mcl
 */
@Controller
@RequestMapping(value = "/ptApprove")
public class PTApproveController {
	private static final long serialVersionUID = 1L;
	
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
   	DiscountDefaultService discountdefaultService;
    @Autowired
   	ConsignmentService consignmentService;
    @Autowired
   	CountryService countryService;
    @Autowired
   	SpecificCountryService specificCountryService;
    @Autowired
   	SpecificConsignmentSetService specificConsignmentSetService;
    @Autowired
   	GeoSummaryService geoSummaryService;
    @Autowired
    ZoneSummaryService zoneSummaryService;
    @Autowired
    CountryGeoService countryGeoService;
    @Autowired
    CountryZoneService countryZoneService;
    @Autowired
    ExamService examService;
    @Autowired
    PtApproveService approveService;
    @Autowired
    RevService revService;
    @Autowired
    HWRateService hwRateService;
    @Autowired
    FsiService fsiService;
    @Autowired
    TariffGroupService tariffGroupService;
    @Autowired
    AccountService accountService;
    /**
	 * BSM 和 RSM approve的方法
	 * @param model
	 * @return
	 */
	@RequestMapping(value="salesAdminApprove", method = RequestMethod.POST)
	public String salesAdminApprove(HttpServletRequest request,Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String state = "";
			if(user!=null){
				if(user.getRole_name()!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[0])){
					state = PTPARAMETERS.PROCESS_SATE[1];
				}else if(user.getRole_name()!=null&&user.getRole_name().equals(PTPARAMETERS.ROLE_NAME[1])){
					state = PTPARAMETERS.PROCESS_SATE[2];
				}
			}
			Business business = new Business();
			business.setState(state);business.setId(Long.parseLong(businessVO.getId()));
			Exam exam = new Exam();
			exam.setExamOppion(businessVO.getExamOppion());
			exam.setBusinessId(Long.parseLong(businessVO.getId()));
			exam.setExamTime(new Date());
			exam.setUserName(user.getRealName());//取当前用户id
			examService.insertExam(exam);
			businessService.updateState(business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/summaryInfoProgress";
	}
	
	/**
	 * 到billing提交页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toBilling", method = RequestMethod.POST)
	public String toBilling(HttpServletRequest request,Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			String state =  PTPARAMETERS.PROCESS_SATE[4];
			Business business = new Business();
			String fileName = businessVO.getFileName();
			Long busiId = Long.valueOf(businessVO.getId());
			business.setState(state);business.setId(busiId);
			businessService.updateState(business);
			String filePath = examService.getFilePath(busiId, getMD5(fileName));
			User user = (User) request.getSession().getAttribute("user");
			Exam exam = new Exam();
			exam.setExamOppion(state);
			exam.setBusinessId(busiId);
			exam.setExamTime(new Date());
			exam.setUserName(user.getRealName());//取当前用户id
			exam.setFilePath(filePath);
			exam.setFileName(fileName);
			
			examService.insertExam(exam);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/tariffPTApprove";
	}
	
	/**
	 * billing center 提交的方法
	 * @param model
	 * @return
	 */
	@RequestMapping(value="billingApprove", method = RequestMethod.POST)
	public String billingApprove(HttpServletRequest request,Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String state =  PTPARAMETERS.PROCESS_SATE[5];
			Long busId = Long.parseLong(businessVO.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Business business = businessService.getBusiness(busId);
			business.setState(state);business.setEffectiveDate(sdf.parse(businessVO.getEffectiveDate()));
			business.setId(busId);
			if(!"".equals(businessVO.getAccount())){
				Customer cus = customerService.getCustomer(business.getCustomerId());
				cus.setAccount(businessVO.getAccount());
				CustomerLog cusLog = new CustomerLog();
				cusLog.setBusId(busId);cusLog.setOldCustomer("777777777");cusLog.setNewCustomer(businessVO.getAccount());
				cusLog.setUserName(user.getRealName());cusLog.setDescribeString(user.getRealName()+"将新客户77777777的账号修改为了"+businessVO.getAccount());
				examService.insertCusLog(cusLog);
				customerService.update(cus);
			}
			Exam exam = new Exam();
			exam.setExamOppion(businessVO.getExamOppion());
			exam.setBusinessId(busId);
			exam.setExamTime(new Date());
			exam.setUserName(user.getRealName());//取当前用户id
			examService.insertExam(exam);
			businessService.updateState(business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/tariffPTBillingApprove";
	}
	
	@RequestMapping(value="modifyEffectiveDate", method = RequestMethod.POST)
	public String modifyEffectiveDate(Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Business business = new Business();
			business.setEffectiveDate(sdf.parse(businessVO.getEffectiveDate()));
			business.setId(Long.parseLong(businessVO.getId()));
			businessService.modifyEffectiveDate(business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/tariffPTBillingApprove";
	}
	
	/**
	 * 进入分析pt 页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptAnalysing", method = RequestMethod.POST)
	public String ptAnalysing(Model model,@RequestParam(value = "id", required = false) Long businessId) {
		try {
			Business business = businessService.getBusiness(businessId);
			Customer cus = customerService.getCustomer(business.getCustomerId());
			model.addAttribute("customer", cus);
			model.addAttribute("business", business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/PTAnalysing";
	}
	
	/**
	 * 由commercial确认  点击confirm后最后返回给sales
	 * @param model
	 * @return
	 */
	@RequestMapping(value="confirmCus", method = RequestMethod.POST)
	public String confirmCus(Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			Business business = businessService.getBusiness(Long.parseLong(businessVO.getId()));
			Customer cus = customerService.getCustomer(business.getCustomerId());
			String fileName = businessVO.getFileName();
			String filePath = examService.getFilePath(Long.parseLong(businessVO.getId()), getMD5(fileName));
			businessVO.setFileName(fileName);
			businessVO.setFilePath(filePath);
			model.addAttribute("customer", cus);
			model.addAttribute("business", business);
			model.addAttribute("businessVO", businessVO);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/confirmCus";
	}
	
	/**
	 * billing center 提交的方法
	 * @param model
	 * @return
	 */
	@RequestMapping(value="commercialConfirm", method = RequestMethod.POST)
	public String commercialConfirm(HttpServletRequest request,Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String state =  PTPARAMETERS.PROCESS_SATE[3];
			Business business = new Business();
			business.setState(state);
			business.setId(Long.parseLong(businessVO.getId()));
			Exam exam = new Exam();
			exam.setExamOppion(businessVO.getExamOppion());
			exam.setBusinessId(Long.parseLong(businessVO.getId()));
			exam.setExamTime(new Date());
			exam.setUserName(user.getRealName());//取当前用户id
			exam.setFilePath(businessVO.getFilePath());
			exam.setFileName(businessVO.getFileName());
			examService.insertExam(exam);
			businessService.updateState(business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/confirmCus";
	}
	
	/**
	 * 审批拒绝通用方法
	 * @param model
	 * @return
	 */
	@RequestMapping(value="reject", method = RequestMethod.POST)
	public String reject(HttpServletRequest request,Model model,@ModelAttribute BusinessVO businessVO) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Business business = new Business();
			business.setState(PTPARAMETERS.PROCESS_SATE_FAIL[0]);business.setId(Long.parseLong(businessVO.getId()));
			Exam exam = new Exam();
			exam.setExamOppion(businessVO.getExamOppion());
			exam.setBusinessId(Long.parseLong(businessVO.getId()));
			exam.setExamTime(new Date());
			exam.setUserName(user.getRealName());//取当前用户id
			examService.insertExam(exam);
			businessService.updateState(business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/tariffPTBillingApprove";
	}
	
	/**
	 * 分析页面点击review调用方法
	 * @param model
	 * @return
	 */
	@RequestMapping(value="review", method = RequestMethod.GET)
	public String review(Model model,@ModelAttribute BusinessVO businessVO) {
		List<RevVO> sendReviewList = new ArrayList<RevVO>();
		List<RevVO> recieveReviewList = new ArrayList<RevVO>();
		List<String> showList = new ArrayList<String>();
		String flag = "";String payMent = "";
		String errorMsg = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("0.##");
		try {
//			String[] groupBy = businessVO.getMultichoised().split(",");
			String[] showGroupBy = businessVO.getMultichoised().split(",");
			String[] groupBy = {"","Product", "PT Zone", "WeightBand","Country"};
			for (String str : showGroupBy) {
				if(!str.equals("")){
					showList.add(str);
				}
			}
			Business business = businessService.getBusiness(Long.parseLong(businessVO.getId()));
			Customer cus = customerService.getCustomer(business.getCustomerId());
			flag = cus.getPayment();
			List<String> r;
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[0]);
				recieveReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[1]);payMent = "SP";
			}else if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[2]);
				recieveReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[2]);payMent = "SP";
			}else{
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, cus.getPayment());
				if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[0])){
					payMent = "SP";
				}else{
					payMent = "RP";
				}
			}
			CostCalculator_Service service = new CostCalculator_Service();
			CostCalculator cc = service.getCostCalculatorPort();
			//新建一个存放total合计的对象
			RevVO rev = new RevVO();
			Map map = new HashMap();
			for(RevVO view:sendReviewList){
				if(view.getChargeableWeight().equals(".5")){
					view.setChargeableWeight("0.5");
				}
				if(view.getCons()==0) sendReviewList.remove(view);
				if(view.getCons()!=0){
					view.setWpc(Double.parseDouble(df.format(view.getKilo()/view.getCons())));
					view.setRpc(Double.parseDouble(df.format(view.getRev()/view.getCons())));
				}
				if(view.getKilo()!=0){
					view.setRpk(Double.parseDouble(df.format(view.getRev()/view.getKilo())));
				}
				rev.setCons(view.getCons()+rev.getCons());
				rev.setKilo(view.getKilo()+rev.getKilo());
				rev.setRev(view.getRev()+rev.getRev());
				rev.setRpc(view.getRpc()+rev.getRpc());
				rev.setRpk(view.getRpk()+rev.getRpk());
				rev.setWpc(view.getWpc()+rev.getWpc());
				map.put("chargeableweight", view.getChargeableWeight());
				map.put("countryCode", view.getCountryCode());
				String itemsNum = accountService.getItemNum(map);
				/*if(view.getChargeableWeight().equals("25")&&view.getCountryCode().equals("AF")){
					System.out.println("   333333333  ");
				}*/
				if(Double.parseDouble(view.getChargeableWeight())<20)  itemsNum="1";
				if(view.getCons()/20-business.getConsStop()>0){
					//计算 DM  FM
					r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), payMent, "CN", business.getDepotCode(), view.getCountryCode(), 
							view.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(), itemsNum,business.getConsStop()+"", Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
				}else{
					//计算 DM  FM
					r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), payMent, "CN", business.getDepotCode(), view.getCountryCode(), 
							view.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(), itemsNum,view.getCons()/20+"", Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
//					String str = "cc.getTotalCosts("+cus.getAccount()+","+sdf.format(business.getApplicationDate())+","+payMent+",CN,"+business.getDepotCode()+","+ view.getCountryCode()+","+ view.getDepotCode()+","+  view.getPRODUCTNAME()+","
//							+  view.getChargeableWeight()+","+ itemsNum+","+view.getCons()+"H,0OKM,[uytgh98}TR54,"+ cus.getChannel()+")";
//					System.out.println(str);
				}
				// 取绝对值
				view.setDm((view.getRev()-Double.parseDouble(r.get(1))*view.getCons())/view.getRev());//r.get(0)
				view.setFm((view.getRev()-Double.parseDouble(r.get(0))*view.getCons())/view.getRev());//r.get(1)
				view.setDm(Double.parseDouble(df.format(view.getDm()))*100);
				view.setFm(Double.parseDouble(df.format(view.getFm()))*100);
			}
			rev.setWpc(Double.parseDouble(df.format(rev.getWpc())));
			rev.setRpk(Double.parseDouble(df.format(rev.getRpk())));
			rev.setMultichoised("Total");
			sendReviewList.add(rev);
			
			//新建一个存放total合计的对象
			RevVO rev2 = new RevVO();
			for(RevVO view:recieveReviewList){
				if(view.getChargeableWeight().equals(".5")){
					view.setChargeableWeight("0.5");
				}
				if(view.getCons()==0) recieveReviewList.remove(view);
				if(view.getCons()!=0){
					view.setWpc(Double.parseDouble(df.format(view.getKilo()/view.getCons())));
					view.setRpc(Double.parseDouble(df.format(view.getRev()/view.getCons())));
				}
				if(view.getKilo()!=0){
					view.setRpk(Double.parseDouble(df.format(view.getRev()/view.getKilo())));
				}
				rev2.setCons(view.getCons()+rev2.getCons());
				rev2.setKilo(view.getKilo()+rev2.getKilo());
				rev2.setRev(view.getRev()+rev2.getRev());
				rev2.setRpc(view.getRpc()+rev2.getRpc());
				rev2.setRpk(view.getRpk()+rev2.getRpk());
				rev2.setWpc(view.getWpc()+rev2.getWpc());
				map.put("chargeableweight", view.getChargeableWeight());
				map.put("countryCode", view.getCountryCode());
				String itemsNum = accountService.getItemNum(map);
				if(Double.parseDouble(view.getChargeableWeight())<20)  itemsNum="1";
				//计算 DM  FM
				r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), "RP", view.getCountryCode(), view.getDepotCode(), "CN", 
						business.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(), itemsNum,"1",  Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
				// 取绝对值
				view.setDm((view.getRev()-Double.parseDouble(r.get(1))*view.getCons())/view.getRev());//r.get(0)
				view.setFm((view.getRev()-Double.parseDouble(r.get(0))*view.getCons())/view.getRev());//r.get(1)
				view.setDm(Double.parseDouble(df.format(view.getDm()*100)));
				view.setFm(Double.parseDouble(df.format(view.getFm()*100)));
			}
			rev2.setWpc(Double.parseDouble(df.format(rev2.getWpc())));
			rev2.setRpk(Double.parseDouble(df.format(rev2.getRpk())));
			rev2.setMultichoised("Total");
			recieveReviewList.add(rev2);
			model.addAttribute("flag",flag);
			model.addAttribute("errorMsg",errorMsg);
			model.addAttribute("counter",showList.size());
			model.addAttribute("customer", cus);
			model.addAttribute("business", business);
			model.addAttribute("showList",showList);
			model.addAttribute("sendReviewList", sendReviewList);
			model.addAttribute("recieveReviewList", recieveReviewList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/caculateReview";
	}
	
	/**
	 * 导出计算结果
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/exportBatchExcel", method = RequestMethod.GET)
	public String exportBatchExcel(Model model,@ModelAttribute BusinessVO businessVO,HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<RevVO> sendReviewList = new ArrayList<RevVO>();
		List<RevVO> recieveReviewList = new ArrayList<RevVO>();
		List<String> showList = new ArrayList<String>();
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		List<Discount> discountList = new ArrayList<Discount>();
		List<Discount> recDiscountList = new ArrayList<Discount>();
		String flag = "";
		DecimalFormat df = new DecimalFormat("0.##");
		try {
			
			List<Product> productList = new ArrayList<Product>();
			List<ZoneType> zoneTypeList = new ArrayList<ZoneType>();
			List<DiscountDefault> discountDefaultList = new ArrayList<DiscountDefault>();
			Map<String,Long> discountDefaultMap = new HashMap<String,Long>();//形成折扣map 方便查询
			zoneTypeList =  zoneTypeService.getAllZoneType();
			if(zoneTypeList.size()>0){
				ZoneType zoneType = zoneTypeList.get(0);
				productList.add(productService.getProduct(zoneType.getDocument()));
				productList.add(productService.getProduct(zoneType.getNonDocument()));
				productList.add(productService.getProduct(zoneType.getEconomy()));
			}
			discountDefaultList = discountdefaultService.getAllDiscountDefault();
			for (DiscountDefault discountDefault:discountDefaultList) {
				discountDefaultMap.put(discountDefault.getProductId()+"_"+discountDefault.getZoneGroupId(), discountDefault.getDiscount());
			}
			ZoneType zoneType = new ZoneType();
			Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
			Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
			Business business = businessService.getBusiness(Long.parseLong(businessVO.getId()));
			Customer cus = customerService.getCustomer(business.getCustomerId());
			cus = customerService.getCustomer(business.getCustomerId());
			zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
			zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
			documentList = weightBandService.findByProductId(zoneType.getDocument());
			ndocumentList = weightBandService.findByProductId(zoneType.getNonDocument());
			eonomyList = weightBandService.findByProductId(zoneType.getEconomy());
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				discountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[0]);
				recDiscountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[1]);
				for (Discount discount:recDiscountList) {
					recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
				}
			}else if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
				discountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);//都取both
			}else{
				discountList = discountService.getAllDiscountByBusId(business.getId(),cus.getPayment());
			}
			for (Discount discount:discountList) {
				discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			String[] groupBy = {"","Product", "PT Zone", "WeightBand","Country"};
			for (String str : groupBy) {
				if(!str.equals("")){
					showList.add(str);
				}
			}
			flag = cus.getPayment();String payMent = "";
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[0]);
				recieveReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[1]);payMent="SP";
			}else if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[2]);
				recieveReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, PTPARAMETERS.PAYMENT[2]);payMent="SP";
			}else{
				sendReviewList = revService.getGroupBy(Long.parseLong(businessVO.getId()), groupBy, cus.getPayment());
				if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[0])){
					payMent="SP";
				}else{
					payMent="RP";
				}
			}
			CostCalculator_Service service = new CostCalculator_Service();
			CostCalculator cc = service.getCostCalculatorPort();
			List<String> r = new ArrayList<String>();
			Map map = new HashMap();
			//新建一个存放total合计的对象
			for(RevVO view:sendReviewList){
				if(view.getChargeableWeight().equals(".5")){
					view.setChargeableWeight("0.5");
				}
				if(view.getCons()!=0){
					view.setWpc(Double.parseDouble(df.format(view.getKilo()/view.getCons())));
					view.setRpc(Double.parseDouble(df.format(view.getRev()/view.getCons())));
				}
				if(view.getKilo()!=0){
					view.setRpk(Double.parseDouble(df.format(view.getRev()/view.getKilo())));
				}
				map.put("chargeableweight", view.getChargeableWeight());
				map.put("countryCode", view.getCountryCode());
				String itemsNum = accountService.getItemNum(map);
				if(Double.parseDouble(view.getChargeableWeight())<20)  itemsNum="1";
				if(view.getCons()/20-business.getConsStop()>0){
					//计算 DM  FM
					r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), payMent, "CN", business.getDepotCode(), view.getCountryCode(), 
							view.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(),itemsNum,business.getConsStop()+"", Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
				}else{
					//计算 DM  FM
					r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), payMent, "CN", business.getDepotCode(), view.getCountryCode(), 
							view.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(),itemsNum,view.getCons()/20+"", Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
					
				}
				// 取绝对值
				view.setDm(Double.parseDouble(r.get(1))*view.getCons());//r.get(0)
				view.setFm(Double.parseDouble(r.get(0))*view.getCons());//r.get(1)
				view.setDm(Double.parseDouble(df.format(view.getDm())));
				view.setFm(Double.parseDouble(df.format(view.getFm())));
			}
			//新建一个存放total合计的对象
			for(RevVO view:recieveReviewList){
				if(view.getCons()!=0){
					view.setWpc(Double.parseDouble(df.format(view.getKilo()/view.getCons())));
					view.setRpc(Double.parseDouble(df.format(view.getRev()/view.getCons())));
				}
				if(view.getKilo()!=0){
					view.setRpk(Double.parseDouble(df.format(view.getRev()/view.getKilo())));
				}
				map.put("chargeableweight", view.getChargeableWeight());
				map.put("countryCode", view.getCountryCode());
				String itemsNum = accountService.getItemNum(map);
				if(Double.parseDouble(view.getChargeableWeight())<20)  itemsNum="1";
				//计算 DM  FM
				r = cc.getTotalCosts(cus.getAccount(), sdf.format(business.getApplicationDate()), "RP", view.getCountryCode(), view.getDepotCode(), "CN", 
						business.getDepotCode(), view.getPRODUCTNAME(), view.getChargeableWeight(), itemsNum,"1" , Double.parseDouble(view.getChargeableWeight())>=20?"H":"L", "0OKM,[uytgh98}TR54", cus.getChannel());
				// 取绝对值
				view.setDm(Double.parseDouble(r.get(1))*view.getCons());//r.get(0)
				view.setFm(Double.parseDouble(r.get(0))*view.getCons());//r.get(1)
				view.setDm(Double.parseDouble(df.format(view.getDm())));
				view.setFm(Double.parseDouble(df.format(view.getFm())));
			}
			model.addAttribute("flag",flag);
			model.addAttribute("counter",showList.size());
			model.addAttribute("customer", cus);
			model.addAttribute("business", business);
			model.addAttribute("showList",showList);
			model.addAttribute("sendReviewList", sendReviewList);
			model.addAttribute("recieveReviewList", recieveReviewList);
			createExportExcel(sendReviewList,recieveReviewList,response,showList,cus.getPayment(),zoneGroupList,documentList,ndocumentList,eonomyList,discountMap,recDiscountMap,business,cus);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/PTAnalysing";
	}
	
	/**
	 * adjust commercial调整页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="adjust", method = RequestMethod.GET)
	public String adjust(Model model,@ModelAttribute BusinessVO businessVO) {
		
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		List<Discount> discountList = new ArrayList<Discount>();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		
		business = businessService.getBusiness(Long.parseLong(businessVO.getId()));
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		
		//计算zonesummary
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		eonomyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		
		
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("documentList", getWeightBandList(documentList));
		model.addAttribute("ndocumentList", getWeightBandList(ndocumentList));
		model.addAttribute("eonomyList", getWeightBandList(eonomyList));
		String payment =  PTPARAMETERS.PAYMENT[0];
		hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),null);
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			discountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[0]);
			
		}else if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
			discountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[2]);
			payment = PTPARAMETERS.PAYMENT[2];
		}else{
			discountList = discountService.getAllDiscountByBusId(business.getId(),customer.getPayment());
			payment = customer.getPayment();
		}
		for (Discount discount:discountList) {
			discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
		}
		if(hwRateList.size()>2){
			model.addAttribute("hwFlag", "hw");
		}else{
			model.addAttribute("hwFlag", " ");
		}
		model.addAttribute("discountMap", discountMap);
		model.addAttribute("businessId", business.getId());
		model.addAttribute("isFollow", business.getIsFollow());
		model.addAttribute("payment", payment);
		
		return "ptProcess/adjust";
	}
	
	/**
	 * adjust commercial调整页 如果是both 且 不follow
	 * @param model
	 * @return
	 */
	@RequestMapping(value="adjustRec/{id}", method = RequestMethod.GET)
	public String adjustRec(Model model,@PathVariable("id") Long id) {
		
		List<ZoneGroup> zoneGroupList = new ArrayList<ZoneGroup>();
		List<WeightBand> documentList = new ArrayList<WeightBand>();
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		List<Discount> discountList = new ArrayList<Discount>();
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		business = businessService.getBusiness(id);
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		
		//计算zonesummary
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(business.getZoneType());
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		eonomyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		
		
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("zoneGroupList", zoneGroupList);
		model.addAttribute("documentList", getWeightBandList(documentList));
		model.addAttribute("ndocumentList", getWeightBandList(ndocumentList));
		model.addAttribute("eonomyList", getWeightBandList(eonomyList));
		discountList = discountService.getAllDiscountByBusId(business.getId(),PTPARAMETERS.PAYMENT[1]);
		hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),null);
		for (Discount discount:discountList) {
			discountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
		}
		if(hwRateList.size()>2){
			model.addAttribute("hwFlag", "hw");
		}else{
			model.addAttribute("hwFlag", " ");
		}
		model.addAttribute("discountMap", discountMap);
		model.addAttribute("businessId", business.getId());
		model.addAttribute("payment", PTPARAMETERS.PAYMENT[1]);
		return "ptProcess/adjustRec";
	}
	
	/**
	 * 根据weightbandGroup的值 只保留一个weightband
	 * @param wbs
	 * @return
	 */
	public List<WeightBand> getWeightBandList(List<WeightBand> wbs){
		List<WeightBand> wbList = new ArrayList<WeightBand>();
		Map<Long,WeightBand> wbMap = new HashMap<Long,WeightBand>();
		for(WeightBand wb : wbs){
			Long weightBandGroupId = wb.getWeightbandGroupId();
			wbMap.put(weightBandGroupId, wb);
		}
		for (Iterator i = wbMap.values().iterator(); i.hasNext();) {
			wbList.add((WeightBand)i.next());
	    }
		//根据weightbandgroupid值进行排序
		ComparatorWeightBand comparator=new ComparatorWeightBand();
		Collections.sort(wbList, comparator);
		return wbList;
	}
	
	class ComparatorWeightBand implements Comparator{
		 public int compare(Object arg0, Object arg1) {
			 WeightBand weightBand0=(WeightBand)arg0;
			 WeightBand weightBand1=(WeightBand)arg1;
		     //首先比较年龄，如果年龄相同，则比较名字
		    int flag=weightBand0.getWeightbandGroupId().compareTo(weightBand1.getWeightbandGroupId());
		    return flag;
		 }
	}
	
	/**
	 * specificConsignmentSet  详细页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="discountSet", method = RequestMethod.GET)
	public String discountSet(Model model,HttpServletRequest request) {
		return "ptProcess/discountSet";
	}
	
	/**
	 * 重货_城市_折扣  详细页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="hwRateProfile/{id}/{flag}", method = RequestMethod.GET)
	public String hwRateProfile(Model model,@PathVariable("id") Long id,@PathVariable("flag") String flag) {
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		business = businessService.getBusiness(id);
		customer = customerService.getCustomer(business.getCustomerId());
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		String payment = "";
		if("NO".equals(business.getIsFollow())&&flag.equals("yes")){
			payment = PTPARAMETERS.PAYMENT[0];
		//如果是yes 并且 payment 为SenderPay 择进入ReceivePay页面
		}else if("NO".equals(business.getIsFollow())&&flag.equals("no")){
			payment = PTPARAMETERS.PAYMENT[1];
		}else if("YES".equals(business.getIsFollow())){
			payment = "Both";
		}else{
			payment = customer.getPayment();
		}
		
		List<WeightBand> ndocumentList = new ArrayList<WeightBand>();
		List<WeightBand> eonomyList = new ArrayList<WeightBand>();
		String ndocumentIds = new String();
		String eonomyIds = new String();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		Map<String,Double> hwRateMap = new HashMap<String,Double>();//形成折扣map 方便查询
		List<Country> ndocountrys = new ArrayList<Country>();
		List<Country> ecocountrys = new ArrayList<Country>();
		
		ndocumentList = weightBandService.getAllHighWeightBandByProductId(zoneType.getNonDocument());//获取重货
		eonomyList = weightBandService.getAllHighWeightBandByProductId(zoneType.getEconomy());//获取重货
		
		ndocountrys = hwRateService.getCountry(business.getId(), zoneType.getNonDocument());
		ecocountrys = hwRateService.getCountry(business.getId(), zoneType.getEconomy());
		
		for (WeightBand wb : ndocumentList) {
			ndocumentIds +=wb.getId()+";";
		}
		
		for (WeightBand wb : eonomyList) {
			eonomyIds +=wb.getId()+";";
		}
		
		hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),payment);
		for (HWRate hwRate:hwRateList) {
			hwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getProductId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
		}
		
		model.addAttribute("business", business);
		model.addAttribute("customer", customer);
		model.addAttribute("ndocumentList", ndocumentList);
		model.addAttribute("eonomyList", eonomyList);
		model.addAttribute("ndocumentIds", ndocumentIds);
		model.addAttribute("eonomyIds", eonomyIds);
		
		model.addAttribute("eonomy", zoneType.getEconomy());
		model.addAttribute("ndocument", zoneType.getNonDocument());
		model.addAttribute("ndocumentCountrys", ndocountrys);
		model.addAttribute("eonomyCountrys", ecocountrys);
		model.addAttribute("hwRateMap", hwRateMap);
		
		model.addAttribute("isFollow", business.getIsFollow());
		model.addAttribute("payment",payment);
		return "ptProcess/hwRateProfile";
	}
	
	
	/**
	 * 进入分析pt 调整利率之后页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="ptAnalysingAdjust/{id}", method = RequestMethod.GET)
	public String ptAnalysingAdjust(Model model,@PathVariable("id") Long businessId) {
		try {
			Business business = businessService.getBusiness(businessId);
			Customer cus = customerService.getCustomer(business.getCustomerId());
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				updateRev(businessId,PTPARAMETERS.PAYMENT[0],cus.getAccount());
				updateRev(businessId,PTPARAMETERS.PAYMENT[1],cus.getAccount());
			}else if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
				updateRev(businessId,PTPARAMETERS.PAYMENT[2],cus.getAccount());
			}else{
				updateRev(businessId,cus.getPayment(),cus.getAccount());
			}
			model.addAttribute("customer", cus);
			model.addAttribute("business", business);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ptProcess/PTAnalysing";
	}
	
	/**
	 * 进入分析pt 调整利率之后更新rev表数据
	 * @param model
	 * @return
	 */
	public void updateRev(Long businessId,String payment,String account){
		List<Consignment>  consignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<Discount>  discountList = new ArrayList<Discount>();//discount数量集合 批量插入
		List<Tariff>  tariffList = new ArrayList<Tariff>();//tariff数量集合 批量插入
		List<WeightBand>  wpList = new ArrayList<WeightBand>();//WeightBand数量集合 批量插入
		List<Rev>  revList = new ArrayList<Rev>();//Rev数量集合 批量插入
		List<Long> countryIds = new ArrayList<Long>();
		List<Long> defaultCountryIds = new ArrayList<Long>();
		List<CountryZone> countryZones= new ArrayList<CountryZone>();
		List<SpecificConsignmentSet> specificConsignmentSets= new ArrayList<SpecificConsignmentSet>();
		List<HWRate> hwRateList = new ArrayList<HWRate>();
		
		//Map<String,Integer> conMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Double> tariffMap = new HashMap<String,Double>();//形成折扣map 方便查询
		Map<Long,Double> weightBandMap = new HashMap<Long,Double>();//形成折扣map 方便查询
		Map<Long,String> isHighWeightBandMap = new HashMap<Long,String>();//形成折扣map 方便查询
		Map<Long,Long> productMap = new HashMap<Long,Long>();//形成折扣map 方便查询
		Map<String,Double> ratioMap = new HashMap<String,Double>();//形成折扣map 方便查询
		Map<String,Integer> scsConMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Double> hwRateMap = new HashMap<String,Double>();//形成hwRateMap方便查询
		try {
			consignmentList = consignmentService.getAllConsignmentByBusId(businessId, payment);
			hwRateList = hwRateService.getAllHWRateByBusId(businessId, payment);
			for (HWRate hwRate : hwRateList) {
				hwRateMap.put(hwRate.getBusinessId()+"_"+hwRate.getWeightBandId()+"_"+hwRate.getCountryId(), hwRate.getRate());
			}
			//获取所有国家的ratio
			countryZones = countryZoneService.getAllCountryZone();
			for (CountryZone countryZone : countryZones) {
				ratioMap.put(countryZone.getZoneGroupId()+"_"+countryZone.getCountryId(), countryZone.getRatio());
			}
			//获取 特殊国家 的 cons
			specificConsignmentSets = specificConsignmentSetService.getAllspecificConsignmentSetByBusId(businessId,payment);
			for (SpecificConsignmentSet scs : specificConsignmentSets) {
				scsConMap.put(businessId+"_"+scs.getWeightBandId()+"_"+scs.getZoneGroupId()+"_"+scs.getCountryId(), scs.getConsignment());
			}
			/***********计算country************/
			
			/*for (Consignment consignment:consignmentList) {
				conMap.put(consignment.getBusinessId()+"_"+consignment.getWeightBandId()+"_"+consignment.getZoneGroupId(), consignment.getConsignment());
			}*/
			discountList = discountService.getAllDiscountByBusId(consignmentList.get(0).getBusinessId(),payment);
			for (Discount discount:discountList) {
				discountMap.put(discount.getBusinessId()+"_"+discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			tariffList = tariffService.getAllTariff();
			for (Tariff tariff:tariffList) {
				tariffMap.put(tariff.getTariffGroupId()+"_"+tariff.getZoneGroupId(), tariff.getTariff());
			}
			wpList = weightBandService.getAllWeightBand();
			for (WeightBand wp:wpList) {
				weightBandMap.put(wp.getId(), wp.getChargeableWeight());
				productMap.put(wp.getId(), wp.getProductId());
				isHighWeightBandMap.put(wp.getId(), wp.getIsHighWeight());
			}
			Business bus = businessService.getBusiness(consignmentList.get(0).getBusinessId());
			Double fsi = fsiService.getFsi(account,bus.getDepotCode());
			for (Consignment con:consignmentList) {
				//计算chargeableweightband，根据这个值 去匹配tariffGroup的单个值
				WeightBand wb = weightBandService.getWeightBand(con.getWeightBandId());
				//List<TariffGroup> tariffGroupList = new ArrayList<TariffGroup>(); 
				Double chargeWeightBand = wb.getChargeableWeight();//
				TariffGroup tariffGroup  = new TariffGroup();
				//根据weightband 以及chargeweightband的值，假如<=20 则根据 weight，weightBandId，type 三个确定一个对象
				if(chargeWeightBand <= 20){
					tariffGroup = tariffGroupService.getTariffGroupByWeightAndWbIdAndType(chargeWeightBand,wb.getId(),payment);
				}else{
					tariffGroup = tariffGroupService.getTariffGroupByWbIdAndType(wb.getId(),payment);
				}
				//根据tariffkey 获取 tariff值
				String tariffkey = tariffGroup.getId()+"_"+con.getZoneGroupId();
				String key = con.getBusinessId()+"_"+con.getWeightBandId()+"_"+con.getZoneGroupId();
				String hwRatekey = con.getBusinessId()+"_"+con.getWeightBandId();
				
				/**
				 * 一个zone以及一个产品 设置的特殊国家 不能这么简单的取出来
				 * 首先定位 cons 根据 rev.getBusinessId()+"_"+rev.getWeightBandId()+"_"+rev.getZoneGroupId();
				 * 然后得到 每个 cons 下面的特殊国家，如果有 country 从而得出每个country 的rev
				 * 如果没有 就根据 zonetype下的defaultcountry 以及ratio得到每个国家的rev
				 */
/*				if(con.getZoneGroupId()==1){
					System.out.println(11);
				}
*/				countryIds = specificConsignmentSetService.getCountrysInSpec(con.getBusinessId(),con.getWeightBandId(),con.getZoneGroupId(),payment);
				if(countryIds.size()>0){
					for(Long id : countryIds){
						Rev rev = new Rev();
						Double value = 0.0;
						rev.setBusinessId(con.getBusinessId());
						rev.setKilo(weightBandMap.get(con.getWeightBandId())*scsConMap.get(key+"_"+id)); //con.getConsignment()
						
						if(hwRateMap.get(hwRatekey+"_"+id) != null){
							//针对country的重货的rev
							value = hwRateMap.get(hwRatekey+"_"+id)*chargeWeightBand*scsConMap.get(key+"_"+id)*(1+fsi);
						}else{
							//假如是重货.
							if("0".equals(isHighWeightBandMap.get(con.getWeightBandId()))){
								value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*chargeWeightBand*scsConMap.get(key+"_"+id)*(1+fsi));//full tariff *（1－dis％off）*CONS*chargeWeightBand*（1+fsi%）   --con.getConsignment()*
							}else{
								value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*scsConMap.get(key+"_"+id)*(1+fsi));//tariff*(1-discount)*con*（1+fsi%） --con.getConsignment()*
							}
						}
						
						rev.setRev(value);
						rev.setWeightBandId(con.getWeightBandId());
						rev.setZoneGroupId(con.getZoneGroupId());
						rev.setCons(scsConMap.get(key+"_"+id).doubleValue()); //con.getConsignment().doubleValue()
						rev.setProductId(productMap.get(con.getWeightBandId()));
						rev.setCountryId(id);
						rev.setPayment(payment);
						revList.add(rev);
					}
					
				}else{
					//如果没有设置特殊国家，就根据默认国家的ratio来获取
					defaultCountryIds = countryZoneService.getAllCountryByZoneGroup(con.getZoneGroupId());
					Double fullPercent = 1.00;
					for (int i=0,len=defaultCountryIds.size();i<len;i++) {
						Rev rev = new Rev();
						Double value = 0.0;
						Long id = defaultCountryIds.get(i);
						Double ratio  = ratioMap.get(con.getZoneGroupId()+"_"+id)*0.01;
						Integer cons = con.getConsignment();
						if(i < len-1){
							rev.setBusinessId(con.getBusinessId());
							rev.setCountryId(id);
							rev.setKilo(weightBandMap.get(con.getWeightBandId())*cons*ratio);
							if(hwRateMap.get(hwRatekey+"_"+id) != null){
								//针对country的重货的rev  hwrate
								value = hwRateMap.get(hwRatekey+"_"+id)*chargeWeightBand*cons*ratio*(1+fsi);
							}else{
								//假如是在weightband标记为重货（0）.
								if("0".equals(isHighWeightBandMap.get(con.getWeightBandId()))){
									value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*chargeWeightBand*cons*ratio*(1+fsi));//full tariff *（1－dis％off）*CONS*chargeWeightBand*（1+fsi%）   --con.getConsignment()*
								}else{
									value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*cons*ratio*(1+fsi));//tariff*(1-discount)*con*（1+fsi%） --con.getConsignment()*
								}
							}
							rev.setRev(value);
							rev.setCons(DoubleUtil.get2Double(cons*ratio));
							rev.setWeightBandId(con.getWeightBandId());
							rev.setZoneGroupId(con.getZoneGroupId());
							rev.setProductId(productMap.get(con.getWeightBandId()));
							rev.setPayment(payment);
							revList.add(rev);
						}else{
							/**
							 * fullpercent =1-ratio1-ratio2-ratio3..
							 */
							rev.setBusinessId(con.getBusinessId());
							rev.setCountryId(id);
							rev.setKilo(weightBandMap.get(con.getWeightBandId())*cons*fullPercent);
							
							if(hwRateMap.get(hwRatekey+"_"+id) != null){
								//针对country的重货的rev  hwrate
								value = hwRateMap.get(hwRatekey+"_"+id)*chargeWeightBand*cons*fullPercent*(1+fsi);
							}else{
								//假如是在weightband标记为重货（0）.
								if("0".equals(isHighWeightBandMap.get(con.getWeightBandId()))){
									value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*chargeWeightBand*cons*fullPercent*(1+fsi));//full tariff *（1－dis％off）*CONS*chargeWeightBand*（1+fsi%）   --con.getConsignment()*
								}else{
									value = DoubleUtil.get2Double(tariffMap.get(tariffkey)*(1-discountMap.get(key)*0.01)*cons*fullPercent*(1+fsi));//tariff*(1-discount)*con*（1+fsi%） --con.getConsignment()*
								}
							}
							rev.setRev(value);
							rev.setCons(DoubleUtil.get2Double(cons*fullPercent));
							rev.setWeightBandId(con.getWeightBandId());
							rev.setZoneGroupId(con.getZoneGroupId());
							rev.setProductId(productMap.get(con.getWeightBandId()));
							rev.setPayment(payment);
							revList.add(rev);
						}
						fullPercent = fullPercent - ratio;
					}
				}
				
			}
			revService.add(revList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="updateConsignment/{payment}", method = {RequestMethod.POST })
	@ResponseBody 
	public String updateConsignment(@RequestBody String jsonDatas,@PathVariable("payment") String payment) {
		String msg = "";
		List<JsonData> jsonDataList = new ArrayList<JsonData>();
		List<Discount>  discountList = new ArrayList<Discount>();//折扣集合 批量插入
		try {
			JSONArray array = new JSONArray(jsonDatas); 
			for(int i = 0; i < array.length(); i++) {  
				JsonData jsonData = JsonMapper.nonDefaultMapper().fromJson(array.getString(i), JsonData.class);  
                jsonDataList.add(jsonData);  
            }  
			for (JsonData jsonData:jsonDataList) {
				String name = jsonData.getName();
				String value = jsonData.getValue();
				if(value.contains(".")){
					throw new Exception("value can not be decimal!");
				}
				String[] discountArr = name.split("_");
				WeightBand wb = weightBandService.getWeightBand(Long.valueOf(discountArr[1]));
				Long weightbandGroupId = wb.getWeightbandGroupId();
				Long productId = wb.getProductId();
				List<WeightBand> wbs = weightBandService.getAllWeightBandByProductIdAndGroupId(productId,weightbandGroupId);
				
				for(WeightBand wb_ : wbs){
					Discount dd = new Discount();
					dd.setWeightBandId(wb_.getId());
					dd.setZoneGroupId(Long.valueOf(discountArr[2]));
					dd.setDiscount(Long.valueOf((value==null||"".equals(value))?"0":value));
					dd.setBusinessId(Long.valueOf(discountArr[3]));
					dd.setPayment(payment);
					discountList.add(dd);
				}
				
			}
			discountService.add(discountList);
		} catch (ParseException e) {
			msg = e.getMessage();
		}catch (Exception e) {
			msg = e.getMessage();
		} 
		return msg;
	}
	
	/**
	 * 公斤_时区_折扣  详细页 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tariffPTApprove/{id}", method = RequestMethod.GET)
	public String rateDetail(Model model,@PathVariable("id") Long  busiId) {
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
		Map<String,Double> rateMap = new HashMap<String,Double>();//形成价格map 方便查询
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		//cons 取件数
		Map<String,Integer> consignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Integer> recConsignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		List<Consignment>  consignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<Consignment>  recConsignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		ZoneSummary zoneSummary = new ZoneSummary();
		ZoneSummary recZoneSummary = new ZoneSummary();
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		String flag = "";//标识是否需要两套数据展示
		business = businessService.getBusiness(busiId);//1L为保存后获得的PT业务主表id
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		customer = customerService.getCustomer(business.getCustomerId());//客户信息
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(zoneType.getZoneType());
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		economyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		flag = customer.getPayment();
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			discountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[0]);
			recDiscountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[1]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[0]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[1]);
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
			discountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[2]);//都取both
			recDiscountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
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
			discountList = discountService.getAllDiscountByBusId(busiId,customer.getPayment());
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,customer.getPayment());
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
		model.addAttribute("discountMap", discountMap);
		model.addAttribute("recDiscountMap", recDiscountMap);
		model.addAttribute("zoneSummary", zoneSummary);
		model.addAttribute("recZoneSummary", recZoneSummary);
	
		model.addAttribute("consignmentMap", consignmentMap);
		model.addAttribute("recConsignmentMap", recConsignmentMap);
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
		
		return "ptProcess/tariffPTApprove";
	}
	
	/**
	 * 公斤_时区_折扣  详细页 审批过程中查看使用
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tariffPTBillingApprove/{id}", method = RequestMethod.GET)
	public String tariffPTBillingApprove(Model model,@PathVariable("id") Long busiId) {
		
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
		Map<String,Double> rateMap = new HashMap<String,Double>();//形成价格map 方便查询
		Map<String,Long> discountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		Map<String,Long> recDiscountMap = new HashMap<String,Long>();//形成折扣map 方便查询
		//cons 取件数
		Map<String,Integer> consignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		Map<String,Integer> recConsignmentMap = new HashMap<String,Integer>();//形成折扣map 方便查询
		List<Consignment>  consignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		List<Consignment>  recConsignmentList = new ArrayList<Consignment>();//con数量集合 批量插入
		ZoneSummary zoneSummary = new ZoneSummary();
		ZoneSummary recZoneSummary = new ZoneSummary();
		Business business = new Business();
		ZoneType zoneType = new ZoneType();
		Customer customer = new Customer();
		String flag = "";//标识是否需要两套数据展示
		business = businessService.getBusiness(busiId);//1L为保存后获得的PT业务主表id
		zoneType = zoneTypeService.getZoneTypeByZoneType(business.getZoneType());//zonetype类型
		customer = customerService.getCustomer(business.getCustomerId());//客户信息
		zoneGroupList =  zonegroupService.getAllZoneGroupByZoneType(zoneType.getZoneType());
		documentList = weightBandService.getAllWeightBandByProductId(zoneType.getDocument());
		ndocumentList = weightBandService.getAllWeightBandByProductId(zoneType.getNonDocument());
		economyList = weightBandService.getAllWeightBandByProductId(zoneType.getEconomy());
		flag = customer.getPayment();
		if(customer.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
			discountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[0]);
			recDiscountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[1]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[0]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[1]);
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
			discountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[2]);//都取both
			recDiscountList = discountService.getAllDiscountByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
			for (Discount discount:recDiscountList) {
				recDiscountMap.put(discount.getWeightBandId()+"_"+discount.getZoneGroupId(), discount.getDiscount());
			}
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
			recConsignmentList = consignmentService.getAllConsignmentByBusId(busiId,PTPARAMETERS.PAYMENT[2]);
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
			discountList = discountService.getAllDiscountByBusId(busiId,customer.getPayment());
			consignmentList = consignmentService.getAllConsignmentByBusId(busiId,customer.getPayment());
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
		model.addAttribute("discountMap", discountMap);
		model.addAttribute("recDiscountMap", recDiscountMap);
		model.addAttribute("zoneSummary", zoneSummary);
		model.addAttribute("recZoneSummary", recZoneSummary);
		model.addAttribute("consignmentMap", consignmentMap);
		model.addAttribute("recConsignmentMap", recConsignmentMap);
		
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
		
		return "ptProcess/tariffPTBillingApprove";
	}

	/**
	 * 完成pt的保存动作
	 * @param jsonDatas
	 * @return
	 */
	@RequestMapping(value="salesSubmit", method = {RequestMethod.GET })
	@ResponseBody 
	public String salesSubmit(HttpServletRequest request) {
		String msg = "";
		/**
		 * 从页面获取数据
		 */
		String businessId = request.getParameter("businessId");
		if("".equals(businessId)){
			msg = "BusinessId is empty ，pls check again";
			return msg;
		}else{
			msg = "Submit success!";
		}
		Long busId = Long.valueOf(businessId);
		Business business = businessService.getBusiness(busId);
		/**
		 * 只有状态为creating 状态才会修改为created，其他的不变
		 */
		business.setState(PTPARAMETERS.PROCESS_SATE[6]);
		User user = (User) request.getSession().getAttribute("user");
		business.setId(busId);
		Exam exam = new Exam();
		exam.setExamOppion(business.getState());
		exam.setBusinessId(busId);
		exam.setExamTime(new Date());
		exam.setUserName(user.getRealName());//取当前用户id
		examService.insertExam(exam);
		//修改state
		businessService.updateState(business);
		return msg;
	}
	
    private transient Session session;
	
    /**
     * 群发邮件
     * 
     * @param recipients
     *                收件人们
     * @param subject
     *                主题
     * @param content
     *                内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(List<String> recipients, String subject, Object content)
        throws AddressException, MessagingException {
    // 创建mime类型邮件
    final MimeMessage message = new MimeMessage(session);
    // 设置发信人
//    message.setFrom(new InternetAddress(authenticator.getUsername()));
    // 设置收件人们
    final int num = recipients.size();
    InternetAddress[] addresses = new InternetAddress[num];
    for (int i = 0; i < num; i++) {
        addresses[i] = new InternetAddress(recipients.get(i));
    }
    message.setRecipients(RecipientType.TO, addresses);
    // 设置主题
    message.setSubject(subject);
    // 设置邮件内容
    message.setContent(content.toString(), "text/html;charset=utf-8");
    // 发送
    Transport.send(message);
    }

    /**
	 * 导出excel
	 * @param model
	 * @return
	 */
    public void createExportExcel(List<RevVO> sendlist,List<RevVO> recievelist,HttpServletResponse response,List<String> showList,String payMent,
    		List<ZoneGroup> zoneGroupList,List<WeightBand> documentList,List<WeightBand> ndocumentList,List<WeightBand> eonomyList,Map<String,Long> discountMap,
   		 Map<String,Long> recDiscountMap,Business business,Customer cus){
		HSSFWorkbook workbook = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			workbook = new HSSFWorkbook();//创建工作薄
			List<HWRate> hwRateList = new ArrayList<HWRate>();
			DecimalFormat df = new DecimalFormat("0.##");
			int showSize = showList.size();
			if(payMent.equals(PTPARAMETERS.PAYMENT[2])){//both  则需要生成两个sheet，否则是一个
				HSSFSheet sheet = workbook.createSheet();
				workbook.setSheetName(0, "Review");
				HSSFFont font = workbook.createFont();  
				font.setColor(Font.COLOR_RED);
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				HSSFCellStyle cellStyle = workbook.createCellStyle();//创建格式
				cellStyle.setFont(font);
				List<String> exportList = new ArrayList<String>();
				exportList.add("TOP");
				for(String str:showList){exportList.add(str); }
				exportList.add("Depot");
				exportList.add("Rev");exportList.add("Cons");exportList.add("Kilo");exportList.add("RPC");
				exportList.add("RPK");exportList.add("WPC");exportList.add("FM");
				exportList.add("DM");exportList.add("Variable Direct Cost");exportList.add("Direct Cost");exportList.add("HW FLAG");
				HSSFRow row = sheet.createRow((short) 0);//第一行 表头
				for(short i = 0;i < exportList.size();i++){   
					HSSFCell cell = row.createCell(i);      //创建第1行单元格   
				    cell.setCellValue(exportList.get(i));
				    cell.setCellStyle(cellStyle);
				}
		        //第二行开始是数据
		        for(int i=1;i<sendlist.size()+1;i++){
		        	HSSFRow rowdata = sheet.createRow((short) i);
		        	HashMap<Integer, String> cellMap = new HashMap<Integer, String>();
		        	cellMap.put(0, "SP");
		        	for(int k=0;k<showSize;k++){
		        		String str = showList.get(k);
		        		if(str.equals("Country")){
		        			cellMap.put(k+1, sendlist.get(i-1).getCountryCode()+"");
		        		}if(str.equals("Product")){
		        			cellMap.put(k+1, sendlist.get(i-1).getPRODUCTNAME()+"");
		        		}if(str.equals("PT Zone")){
		        			cellMap.put(k+1, sendlist.get(i-1).getZone()+"");
		        		}if(str.equals("WeightBand")){
		        			cellMap.put(k+1, sendlist.get(i-1).getWEIGHTNAME()+"");
		        		}
		        	}
		        	cellMap.put(showSize+1, sendlist.get(i-1).getDepotCode());
		        	cellMap.put(showSize+2, sendlist.get(i-1).getRev()+"");cellMap.put(showSize+8, df.format(sendlist.get(i-1).getRev()-sendlist.get(i-1).getFm()));
		        	cellMap.put(showSize+3, sendlist.get(i-1).getCons()+"");cellMap.put(showSize+4, sendlist.get(i-1).getKilo()+"");
		        	cellMap.put(showSize+5, sendlist.get(i-1).getRpc()+"");cellMap.put(showSize+6, sendlist.get(i-1).getRpk()+"");
		        	cellMap.put(showSize+7, sendlist.get(i-1).getWpc()+"");cellMap.put(showSize+9, df.format(sendlist.get(i-1).getRev()-sendlist.get(i-1).getDm()));
		        	cellMap.put(showSize+10, sendlist.get(i-1).getFm()+"");cellMap.put(showSize+11, sendlist.get(i-1).getDm()+"");
		        	hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[0]);
		        	
		        	if(Double.parseDouble(sendlist.get(i-1).getChargeableWeight())-20>0){
		        		cellMap.put(showSize+12, "no");
		        		for (HWRate hwRate:hwRateList) {//2 15N  3 48N
			    			if(hwRate.getProductId().toString().equals("2")&&sendlist.get(i-1).getPRODUCTNAME().equals("15N")){
			    					cellMap.put(showSize+12, "yes");break;
			    			}
			    			if(hwRate.getProductId().toString().equals("3")&&sendlist.get(i-1).getPRODUCTNAME().equals("48N")){
		    					cellMap.put(showSize+12, "yes");break;
			    			}
			    		}
		        	}else{
		        		cellMap.put(showSize+12, "no");
		        	}
		        	for (int j = 0; j < exportList.size(); j++) {
		    			HSSFCell celldata = rowdata.createCell((short) j);      // 在上面行索引0的位置创建单元格
		    			celldata.setCellType(Cell.CELL_TYPE_STRING);     	// 定义单元格为字符串类型
		    			celldata.setCellValue(cellMap.get(j)+ "");
					}
		        }
		        //第二行开始是数据
		        for(int i=1;i<recievelist.size()+1;i++){
		        	HSSFRow rowdata = sheet.createRow((short) sendlist.size()+i);
		        	HashMap<Integer, String> cellMap = new HashMap<Integer, String>();
		        	cellMap.put(0, "RP");
		        	for(int k=0;k<showSize;k++){
		        		String str = showList.get(k);
		        		if(str.equals("Country")){
		        			cellMap.put(k+1, recievelist.get(i-1).getCountryCode()+"");
		        		}if(str.equals("Product")){
		        			cellMap.put(k+1, recievelist.get(i-1).getPRODUCTNAME()+"");
		        		}if(str.equals("PT Zone")){
		        			cellMap.put(k+1, recievelist.get(i-1).getZone()+"");
		        		}if(str.equals("WeightBand")){
		        			cellMap.put(k+1, recievelist.get(i-1).getWEIGHTNAME()+"");
		        		}
		        	}
		        	cellMap.put(showSize+1, recievelist.get(i-1).getDepotCode());
		        	cellMap.put(showSize+2, recievelist.get(i-1).getRev()+"");cellMap.put(showSize+8, df.format(recievelist.get(i-1).getFm()/recievelist.get(i-1).getRev()));
		        	cellMap.put(showSize+3, recievelist.get(i-1).getCons()+"");cellMap.put(showSize+4, recievelist.get(i-1).getKilo()+"");
		        	cellMap.put(showSize+5, recievelist.get(i-1).getRpc()+"");cellMap.put(showSize+6, recievelist.get(i-1).getRpk()+"");
		        	cellMap.put(showSize+7, recievelist.get(i-1).getWpc()+"");cellMap.put(showSize+9, df.format(recievelist.get(i-1).getDm()/recievelist.get(i-1).getRev()));
		        	cellMap.put(showSize+10, recievelist.get(i-1).getFm()+"");cellMap.put(showSize+11, recievelist.get(i-1).getDm()+"");
		        	hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),PTPARAMETERS.PAYMENT[1]);
		        	if(Double.parseDouble(recievelist.get(i-1).getChargeableWeight())-20>0){
		        		cellMap.put(showSize+12, "no");
		        		for (HWRate hwRate:hwRateList) {//2 15N  3 48N
			    			if(hwRate.getProductId().toString().equals("2")&&sendlist.get(i-1).getPRODUCTNAME().equals("15N")){
			    					cellMap.put(showSize+12, "yes");break;
			    			}
			    			if(hwRate.getProductId().toString().equals("3")&&sendlist.get(i-1).getPRODUCTNAME().equals("48N")){
		    					cellMap.put(showSize+12, "yes");break;
			    			}
			    		}
		        	}else{
		        		cellMap.put(showSize+12, "no");
		        	}
		        	for (int j = 0; j < exportList.size(); j++) {
		    			HSSFCell celldata = rowdata.createCell((short) j);      // 在上面行索引0的位置创建单元格
		    			celldata.setCellType(Cell.CELL_TYPE_STRING);     	// 定义单元格为字符串类型
		    			celldata.setCellValue(cellMap.get(j)+ "");
					}
		        }
			}else{
				HSSFSheet sheet = workbook.createSheet();
				workbook.setSheetName(0, "Review");
				HSSFFont font = workbook.createFont();  
				font.setColor(Font.COLOR_RED);
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				HSSFCellStyle cellStyle = workbook.createCellStyle();//创建格式
				cellStyle.setFont(font);
				List<String> exportList = new ArrayList<String>();
				exportList.add("TOP");
				for(String str:showList){exportList.add(str); }
				exportList.add("Depot");
				exportList.add("Rev");exportList.add("Cons");exportList.add("Kilo");exportList.add("RPC");
				exportList.add("RPK");exportList.add("WPC");exportList.add("FM");
				exportList.add("DM");exportList.add("Variable Direct Cost");exportList.add("Direct Cost");exportList.add("HW FLAG");
				HSSFRow row = sheet.createRow((short) 0);					//第一行 表头
				for(short i = 0;i < exportList.size();i++){   
					HSSFCell cell = row.createCell(i);      //创建第1行单元格   
				    cell.setCellValue(exportList.get(i));
				    cell.setCellStyle(cellStyle);
				}
		        //第二行开始是数据
		        for(int i=1;i<sendlist.size()+1;i++){
		        	HSSFRow rowdata = sheet.createRow((short) i);
		        	HashMap<Integer, String> cellMap = new HashMap<Integer, String>();
		        	cellMap.put(0, payMent);
		        	RevVO revVO = sendlist.get(i-1);
		        	for(int k=0;k<showSize;k++){
		        		String str = showList.get(k);
		        		if(str.equals("Country")){
		        			cellMap.put(k+1, sendlist.get(i-1).getCountryCode()+"");
		        		}if(str.equals("Product")){
		        			cellMap.put(k+1, sendlist.get(i-1).getPRODUCTNAME()+"");
		        		}if(str.equals("PT Zone")){
		        			cellMap.put(k+1, sendlist.get(i-1).getZone()+"");
		        		}if(str.equals("WeightBand")){
		        			cellMap.put(k+1, sendlist.get(i-1).getWEIGHTNAME()+"");
		        		}
		        	}
		        	cellMap.put(showSize+1, revVO.getDepotCode());
		        	cellMap.put(showSize+2, revVO.getRev()+"");cellMap.put(showSize+8, df.format(revVO.getFm()/revVO.getRev()));
		        	cellMap.put(showSize+3, revVO.getCons()+"");cellMap.put(showSize+4, revVO.getKilo()+"");
		        	cellMap.put(showSize+5, revVO.getRpc()+"");cellMap.put(showSize+6, revVO.getRpk()+"");
		        	cellMap.put(showSize+7, revVO.getWpc()+"");cellMap.put(showSize+9, df.format(revVO.getDm()/revVO.getRev()));
		        	cellMap.put(showSize+10, revVO.getFm()+"");cellMap.put(showSize+11, revVO.getDm()+"");
		        	hwRateList = hwRateService.getAllHWRateByBusId(business.getId(),payMent);
		        	if(Double.parseDouble(revVO.getChargeableWeight())-20>0){
		        		cellMap.put(showSize+12, "no");
		        		for (HWRate hwRate:hwRateList) {//2 15N  3 48N
			    			if(hwRate.getProductId().toString().equals("2")&&revVO.getPRODUCTNAME().equals("15N")
			    					&&hwRate.getCountryId().equals(revVO.getCountryId())){
			    					cellMap.put(showSize+12, "yes");break;
			    			}
			    			if(hwRate.getProductId().toString().equals("3")&&revVO.getPRODUCTNAME().equals("48N")
			    					&&hwRate.getCountryId().equals(revVO.getCountryId())){
		    					cellMap.put(showSize+12, "yes");break;
			    			}
			    		}
		        	}else{
		        		cellMap.put(showSize+12, "no");
		        	}
		        	for (int j = 0; j < exportList.size(); j++) {
		    			HSSFCell celldata = rowdata.createCell((short) j);      // 在上面行索引0的位置创建单元格
		    			celldata.setCellType(Cell.CELL_TYPE_STRING);     	// 定义单元格为字符串类型
		    			celldata.setCellValue(cellMap.get(j)+ "");
					}
		        }
			}
	        //另一个sheet放折扣
	        HSSFSheet sheetDiscount = workbook.createSheet();
			workbook.setSheetName(1, "Discount");
			for(int i=0;i<6;i++){
				HSSFRow rowdata = sheetDiscount.createRow((short) i);
				HSSFCell celldata1 = rowdata.createCell((short) 0);      // 在上面行索引0的位置创建单元格
				HSSFCell celldata2 = rowdata.createCell((short) 1);      // 在上面行索引0的位置创建单元格
				celldata1.setCellType(Cell.CELL_TYPE_STRING);celldata2.setCellType(Cell.CELL_TYPE_STRING);
				switch (i) {
				case 0:
					celldata1.setCellValue("Application Date");celldata2.setCellValue(sdf.format(business.getApplicationDate()));
					break;
				case 1:
					celldata1.setCellValue("Depot");celldata2.setCellValue(business.getDepotCode());
					break;
				case 2:
					celldata1.setCellValue("Account Number");celldata2.setCellValue(cus.getAccount());
					break;
				case 3:
					celldata1.setCellValue("Customer Name");celldata2.setCellValue(cus.getCusName());
					break;
				case 4:
					celldata1.setCellValue("Terms of Payment");celldata2.setCellValue(cus.getPayment());
					break;
				case 5:
					celldata1.setCellValue("Reason for the PT");celldata2.setCellValue(business.getReson());
					break;
				default:
					break;
				}
			}
			HSSFRow rowDate = sheetDiscount.createRow(6);
			HSSFCell celldata = rowDate.createCell((short) 0); 
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				celldata.setCellValue("Terms of Payment:	SP");
	    	}else if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("YES")){//如果选择的是both 并且 isfollow为YES  此时需要展示两个同样的tab页
	    		celldata.setCellValue("Terms of Payment:	SP/RP");
	    	}else{
	    		celldata.setCellValue("Terms of Payment:	"+cus.getPayment());
	    	}
			
			HSSFRow rowDate2 = sheetDiscount.createRow(7);
			HSSFCell celldata1 = rowDate2.createCell((short) 0); 
			celldata1.setCellValue("Discount Profile - 15D(% off Full Tariff)");
			HSSFRow rowDate3 = sheetDiscount.createRow(8);
			HSSFCell celldata2 = rowDate3.createCell((short) 0); 
			celldata2.setCellValue("Weightband");
			for(int i=0;i<zoneGroupList.size();i++){
				ZoneGroup zoneGroup  = zoneGroupList.get(i);
				HSSFCell celldata4 = rowDate3.createCell((short) (i+1));
				celldata4.setCellValue(zoneGroup.getZone());
			}
			for(int i=0;i<documentList.size();i++){
				HSSFRow rowDate5 = sheetDiscount.createRow(9+i);
				HSSFCell celldata3 = rowDate5.createCell((short) 0); 
				WeightBand wd = documentList.get(i);
				celldata3.setCellValue(wd.getName());
	        	for(int j=0;j<zoneGroupList.size();j++){
	        		ZoneGroup zoneGroup = zoneGroupList.get(j);
	        		HSSFCell celldata4 = rowDate5.createCell((short) (j+1)); 
	        		String keyId = wd.getId()+"_"+zoneGroup.getId();
	        		celldata4.setCellValue(discountMap.get(keyId)+"%");
	            }
	        }
			
			HSSFRow rowDate4 = sheetDiscount.createRow(9+documentList.size());
			HSSFCell celldata4 = rowDate4.createCell((short) 0); 
			celldata4.setCellValue("Discount Profile - 15N(% off Full Tariff)");
			HSSFRow rowDate5 = sheetDiscount.createRow(10+documentList.size());
			HSSFCell celldata5 = rowDate5.createCell((short) 0); 
			celldata5.setCellValue("Weightband");
			for(int i=0;i<zoneGroupList.size();i++){
				ZoneGroup zoneGroup  = zoneGroupList.get(i);
				HSSFCell celldata6 = rowDate5.createCell((short) (i+1));
				celldata6.setCellValue(zoneGroup.getZone());
			}
			for(int i=0;i<ndocumentList.size();i++){
				HSSFRow rowDate6 = sheetDiscount.createRow(i+11+documentList.size());
				HSSFCell celldata3 = rowDate6.createCell((short) 0); 
				WeightBand wd = ndocumentList.get(i);
				celldata3.setCellValue(wd.getName());
	        	for(int j=0;j<zoneGroupList.size();j++){
	        		ZoneGroup zoneGroup = zoneGroupList.get(j);
	        		HSSFCell celldata6 = rowDate6.createCell((short) (j+1)); 
	        		String keyId = wd.getId()+"_"+zoneGroup.getId();
	        		celldata6.setCellValue(discountMap.get(keyId)+"%");
	            }
	        }
			
			HSSFRow rowDate20 = sheetDiscount.createRow(12+documentList.size()+ndocumentList.size());
			HSSFCell celldata20 = rowDate20.createCell((short) 0); 
			celldata20.setCellValue("Discount Profile - 48N(% off Full Tariff)");
			HSSFRow rowDate21 = sheetDiscount.createRow(13+documentList.size()+ndocumentList.size());
			HSSFCell celldata21 = rowDate21.createCell((short) 0); 
			celldata21.setCellValue("Weightband");
			for(int i=0;i<zoneGroupList.size();i++){
				ZoneGroup zoneGroup  = zoneGroupList.get(i);
				HSSFCell celldata6 = rowDate21.createCell((short) (i+1));
				celldata6.setCellValue(zoneGroup.getZone());
			}
			for(int i=0;i<eonomyList.size();i++){
				HSSFRow rowDate6 = sheetDiscount.createRow(i+14+documentList.size()+ndocumentList.size());
				HSSFCell celldata3 = rowDate6.createCell((short) 0); 
				WeightBand wd = eonomyList.get(i);
				celldata3.setCellValue(wd.getName());
	        	for(int j=0;j<zoneGroupList.size();j++){
	        		ZoneGroup zoneGroup = zoneGroupList.get(j);
	        		HSSFCell celldata6 = rowDate6.createCell((short) (j+1)); 
	        		String keyId = wd.getId()+"_"+zoneGroup.getId();
	        		celldata6.setCellValue(discountMap.get(keyId)+"%");
	            }
	        }
			
			if(cus.getPayment().equals(PTPARAMETERS.PAYMENT[2])&&business.getIsFollow().equals("NO")){//如果选择的是both 并且 isfollow为no  此时需要展示两个tab页
				HSSFRow rowDateR = sheetDiscount.createRow(14+documentList.size()+ndocumentList.size()+eonomyList.size());
				HSSFCell celldataR = rowDateR.createCell((short) 0); 
				celldataR.setCellValue("Terms of Payment:	RP");
				
				HSSFRow rowDate19 = sheetDiscount.createRow(15+documentList.size()+ndocumentList.size()+eonomyList.size());
				HSSFCell celldata19 = rowDate19.createCell((short) 0); 
				celldata19.setCellValue("Discount Profile - 15D(% off Full Tariff)");
				HSSFRow rowDate18 = sheetDiscount.createRow(16+documentList.size()+ndocumentList.size()+eonomyList.size());
				HSSFCell celldata17 = rowDate18.createCell((short) 0); 
				celldata17.setCellValue("Weightband");
				for(int i=0;i<zoneGroupList.size();i++){
					ZoneGroup zoneGroup  = zoneGroupList.get(i);
					HSSFCell celldata16 = rowDate18.createCell((short) (i+1));
					celldata16.setCellValue(zoneGroup.getZone());
				}
				for(int i=0;i<documentList.size();i++){
					HSSFRow rowDate8 = sheetDiscount.createRow(i+17+documentList.size()+ndocumentList.size()+eonomyList.size());
					HSSFCell celldata3 = rowDate8.createCell((short) 0); 
					WeightBand wd = documentList.get(i);
					celldata3.setCellValue(wd.getName());
		        	for(int j=0;j<zoneGroupList.size();j++){
		        		ZoneGroup zoneGroup = zoneGroupList.get(j);
		        		HSSFCell celldata9 = rowDate8.createCell((short) (j+1)); 
		        		String keyId = wd.getId()+"_"+zoneGroup.getId();
		        		celldata9.setCellValue(recDiscountMap.get(keyId)+"%");
		            }
		        }
				
				HSSFRow rowDate17 = sheetDiscount.createRow(18+documentList.size()*2+ndocumentList.size()+eonomyList.size());
				HSSFCell celldata10 = rowDate17.createCell((short) 0); 
				celldata10.setCellValue("Discount Profile - 15N(% off Full Tariff)");
				HSSFRow rowDate16 = sheetDiscount.createRow(19+documentList.size()*2+ndocumentList.size()+eonomyList.size());
				HSSFCell celldata11 = rowDate16.createCell((short) 0); 
				celldata11.setCellValue("Weightband");
				for(int i=0;i<zoneGroupList.size();i++){
					ZoneGroup zoneGroup  = zoneGroupList.get(i);
					HSSFCell celldata6 = rowDate16.createCell((short) (i+1));
					celldata6.setCellValue(zoneGroup.getZone());
				}
				for(int i=0;i<ndocumentList.size();i++){
					HSSFRow rowDate6 = sheetDiscount.createRow(i+20+documentList.size()*2+ndocumentList.size()+eonomyList.size());
					HSSFCell celldata3 = rowDate6.createCell((short) 0); 
					WeightBand wd = ndocumentList.get(i);
					celldata3.setCellValue(wd.getName());
		        	for(int j=0;j<zoneGroupList.size();j++){
		        		ZoneGroup zoneGroup = zoneGroupList.get(j);
		        		HSSFCell celldata6 = rowDate6.createCell((short) (j+1)); 
		        		String keyId = wd.getId()+"_"+zoneGroup.getId();
		        		celldata6.setCellValue(recDiscountMap.get(keyId)+"%");
		            }
		        }
				
				
				HSSFRow rowDate15 = sheetDiscount.createRow(21+documentList.size()*2+ndocumentList.size()*2+eonomyList.size());
				HSSFCell celldata15 = rowDate15.createCell((short) 0); 
				celldata15.setCellValue("Discount Profile - 48N(% off Full Tariff)");
				HSSFRow rowDate14 = sheetDiscount.createRow(22+documentList.size()*2+ndocumentList.size()*2+eonomyList.size());
				HSSFCell celldata14 = rowDate14.createCell((short) 0); 
				celldata14.setCellValue("Weightband");
				for(int i=0;i<zoneGroupList.size();i++){
					ZoneGroup zoneGroup  = zoneGroupList.get(i);
					HSSFCell celldata6 = rowDate14.createCell((short) (i+1));
					celldata6.setCellValue(zoneGroup.getZone());
				}
				for(int i=0;i<eonomyList.size();i++){
					HSSFRow rowDate6 = sheetDiscount.createRow(i+23+documentList.size()*2+ndocumentList.size()*2+eonomyList.size());
					HSSFCell celldata3 = rowDate6.createCell((short) 0); 
					WeightBand wd = eonomyList.get(i);
					celldata3.setCellValue(wd.getName());
		        	for(int j=0;j<zoneGroupList.size();j++){
		        		ZoneGroup zoneGroup = zoneGroupList.get(j);
		        		HSSFCell celldata6 = rowDate6.createCell((short) (j+1)); 
		        		String keyId = wd.getId()+"_"+zoneGroup.getId();
		        		celldata6.setCellValue(recDiscountMap.get(keyId)+"%");
		            }
		        }
	    	}
			
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			// 表示以附件的形式把文件发送到客户端
			response.setHeader("Content-Disposition", "attachment;filename="
						+ new String(((business.getApplicationReference())+".xls").getBytes(), "ISO8859-1"));
			// 通过response的输出流把工作薄的流发送浏览器形成文件
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}   
	}
    
    /**
	 * 文件上传
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveFilePath", method = RequestMethod.POST)
	public void saveFilePath(HttpServletRequest request,@ModelAttribute BusinessVO businessVO) {
		String savePath = request.getSession().getServletContext().getContextPath();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
        savePath = savePath + "/attached/uploads/"+ ymd + "/"+businessVO.getFileName();
		BusinessFile bFile = new BusinessFile();
        bFile.setBusinessId(Long.valueOf(businessVO.getId()));
		bFile.setFileName(businessVO.getFileName());
		bFile.setFilePath(savePath);
		bFile.setUploadDate(new Date());
		String filePathString = ""+examService.getFilePath(Long.valueOf(businessVO.getId()), businessVO.getFileName());
		if(!filePathString.equals(bFile.getFilePath())){
			examService.insertFile(bFile);
		}
	}
    
    /**
	 * 文件上传
	 * @param model
	 * @return
	 */
	@RequestMapping(value="uploadFile/{id}", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request,PrintWriter out,
				Model model,@PathVariable("id") Long  busiId) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();  
		MultipartFile multipartFile =null;
		for(Map.Entry<String,MultipartFile > set:fileMap.entrySet()){
		String filekey = set.getKey();//Filedata
		 multipartFile = set.getValue();//文件名
		}
		Map<String, String> retMap = this.storeIOc(multipartRequest, multipartFile);
		BusinessFile bFile = new BusinessFile();
        bFile.setBusinessId(busiId);
		bFile.setFileName(retMap.get("fileName"));
		bFile.setFilePath(retMap.get("filePath"));
		bFile.setUploadDate(new Date());
		String filePathString = ""+examService.getFilePath(busiId, retMap.get("fileName"));
		if(!filePathString.equals(bFile.getFilePath())){
			examService.insertFile(bFile);
		}
	}
	
	// 接受图片，返回图片地址
	private Map<String, String> storeIOc(HttpServletRequest request, MultipartFile file) {
		Map<String, String> retMap = new HashMap<String, String>();
		String realPath = request.getSession().getServletContext().getRealPath("/attached/uploads/");
		String savePath = request.getSession().getServletContext().getContextPath();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		realPath = realPath  + "/" + ymd + "/";
		File f1 = new File(realPath);
	    System.out.println(realPath);
	    if (!f1.exists()) {
	        f1.mkdirs();
	    }
		String filePath = "";
		if (file.isEmpty()) {
			System.out.println("文件未上传");
		} else {
			String _fileName = file.getOriginalFilename();
			// /**使用UUID生成文件名称**/
//			_fileName = UUID.randomUUID().toString();
			filePath = realPath + File.separator + _fileName;
			File restore = new File(filePath);
			retMap.put("fileName", _fileName);
			savePath = savePath + "/attached/uploads/"+ ymd + "/"+_fileName;
			retMap.put("filePath", savePath);
			try {
				file.transferTo(restore);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	// 返回默认的图片地址
	return retMap;
	}

	
    @RequestMapping(value = "deleteFile", method = RequestMethod.POST)   
    public String deleteFile(Model model,@RequestParam(value = "id", required = false) Long busiId,@RequestParam(value = "fileName", required = false) String fileName){
    	String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String newFileName = getMD5(fileName) + "." + fileExt;
    	String filePathString = examService.getFilePath(busiId, newFileName);
    	//删除文件
    	FileUtil.delFile(filePathString);
    	examService.deleteFile(busiId, newFileName);
        return "ptProcess/summaryInfoProgress";
    }   
	
	private String getMD5(String plainText) { 
		/*String returnStr = "";
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes()); 
			byte b[] = md.digest();
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
			i = b[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
			} 
			returnStr = buf.toString();
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace(); 
		} */
		return plainText; 
	} 
	
}
