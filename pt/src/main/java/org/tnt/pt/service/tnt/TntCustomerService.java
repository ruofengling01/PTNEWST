package org.tnt.pt.service.tnt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tnt.pt.repository.dms.UserDao;
import org.tnt.pt.repository.tnt.TntCustomerDao;
import org.tnt.pt.service.ServiceException;
import org.tnt.pt.tntentity.Customer;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class TntCustomerService {

	private TntCustomerDao tntcustomerDao;
	
	private UserDao userDao;

	public Customer getCustomer(String accountNo,String code) throws ServiceException {
		if("".equals(accountNo)||"".equals(code)){
			throw new ServiceException();
		}
		String rpu = userDao.getRPUbyDepotCode(code);
		if("CNRPU".equals(rpu)){//V_PT_DEPOT中如果RPU是CNRPU就是中国区的，别的就是非中国区
			return tntcustomerDao.findByAccountNo(accountNo);
		}else{
			return userDao.getCusFromHKAccount(accountNo);
		}
		
	}
	
	@Autowired
	public void setTntcustomerDao(TntCustomerDao tntcustomerDao) {
		this.tntcustomerDao = tntcustomerDao;
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	
	
	
	
	
	
}
