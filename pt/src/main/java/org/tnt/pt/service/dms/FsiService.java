package org.tnt.pt.service.dms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tnt.pt.dmsentity.FSI;
import org.tnt.pt.repository.dms.FsiDao;
import org.tnt.pt.service.ServiceException;
import org.tnt.pt.util.DoubleUtil;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class FsiService {

	private FsiDao fsiDao;

	/**
	 *  t_chr_fsf:以account_no,当前时间(匹配from date和to date)，RPU_ID(以用户选择的站点去V_PT_DEPOT中查询RPU_ID，在以这个RPU_ID去匹配燃油附加费中的RPU_ID)字段匹配成功的情况下需要用到此表，有2种情况 
               1）IS_FSI_DIS是1，燃油附加费取FSI_DIS_RATE*（t_chr_fsi表中的当前生效的燃油附加费FS_RATE）->表示此客户是按照浮动燃油附加费打折 
               2）(IS_FSI_DIS是null或者是0)并且FS_RATE不是null，燃油附加费取FS_RATE ->表示此客户是按照固定燃油附加费计算 
t_chr_fsi:申请PT的客户在t_chr_fsf中无法找到记录的时候，就取t_chr_fsi表中当前时间在from_date和to_date之间的创建时间最新的那条记录得燃油附加费FS_RATE 
	 * @param accountNo
	 * @return
	 */
	public Double getFsi(String accountNo,String code) {
		if(accountNo==null) throw new ServiceException("获取燃油附加费时参数错误");
		FSI fsi  = new FSI();
		Double rate = 0.0;
		Double rateFSI = fsiDao.getRateFromFSI()==null?0.0:fsiDao.getRateFromFSI();//取t_chr_fsi表中当前时间在from_date和to_date之间的创建时间最新的那条记录得燃油附加费FS_RATE
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		String rpu_id = fsiDao.getFromDepot(code);//以用户选择的站点去V_PT_DEPOT中查询RPU_ID，在以这个RPU_ID去匹配燃油附加费中的RPU_ID
		parameter.put("accountNo", accountNo);
		parameter.put("rpu_id", rpu_id);
		Integer isFromFsf = fsiDao.isFromFsf(parameter);
		
		if(isFromFsf > 0){
			fsi = fsiDao.getFromFSF(accountNo);//"000009302"
			if("1".equals(fsi.getIs_fsi_dis())){//IS_FSI_DIS是1，燃油附加费取FSI_DIS_RATE*（t_chr_fsi表中的当前生效的燃油附加费FS_RATE）->表示此客户是按照浮动燃油附加费打折 
				rate = fsi.getFsi_dis_rate()==null?0.0:DoubleUtil.get3Double(fsi.getFsi_dis_rate()*rateFSI);
			}else{//(IS_FSI_DIS是null或者是0)并且FS_RATE不是null，燃油附加费取FS_RATE ->表示此客户是按照固定燃油附加费计算 
				rate = fsi.getFs_rate()==null?0.0:fsi.getFs_rate();
			}
		}else{
			rate = rateFSI;//fsiDao.getRateFromFSI()==null?0.0:fsiDao.getRateFromFSI();
		}
		return rate;
	}

	public FsiDao getFsiDao() {
		return fsiDao;
	}

	@Autowired
	public void setFsiDao(FsiDao fsiDao) {
		this.fsiDao = fsiDao;
	}
	
}
