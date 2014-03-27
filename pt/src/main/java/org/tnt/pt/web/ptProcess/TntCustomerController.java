package org.tnt.pt.web.ptProcess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;
import org.tnt.pt.service.dms.FsiService;
import org.tnt.pt.service.tnt.TntCustomerService;
import org.tnt.pt.tntentity.Customer;


@Controller
@RequestMapping(value = "/tntCustomer")
public class TntCustomerController {
	 @Autowired
	 TntCustomerService tntcustomerService;
	 @Autowired
	 FsiService fsiService;
	 
	 /**
		 * 新建页面copy
		 * @param model
		 * @return
		 */
	@RequestMapping(value="getCustomer/{accountNo}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public String getCustomer(Model model,@PathVariable("accountNo") String accountNo
			,@PathVariable("code") String code) {
		Customer cus = tntcustomerService.getCustomer(accountNo,code);
		String cusstr = JsonMapper.nonDefaultMapper().toJson(cus);
		return cusstr;
	}
	
	 /**
	 * 新建页面copy
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getFsi/{accountNo}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public String getFsi(Model model,@PathVariable("accountNo") String accountNo
			,@PathVariable("code") String code) {
		Double fsi = fsiService.getFsi("000009305", "SHA");
		System.out.println(fsi);
		String fsiStr = fsi+"";
		return fsiStr;
	}
}
