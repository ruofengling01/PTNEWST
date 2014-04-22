package org.tnt.pt.repository;

import java.util.List;
import java.util.Map;

import org.tnt.pt.entity.Business;
import org.tnt.pt.entity.BusinessFile;
import org.tnt.pt.entity.CustomerLog;
import org.tnt.pt.entity.Exam;
import org.tnt.pt.entity.Tariff;
import org.tnt.pt.vo.BaseVO;
import org.tnt.pt.vo.BusinessVO;
import org.tnt.pt.vo.HWRateVO;

public class ExamDaoImpl implements ExamDao{

	@Override
	public Integer insert(Exam t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Exam t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBySelective(Exam t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Exam selectById(Long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectByMapCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectByVOCount(BaseVO baseVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Exam> selectByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertFile(BusinessFile bFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertCusLog(CustomerLog cusLog) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(Map map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFilePath(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tariff> getTariff(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HWRateVO> getHWRateVO(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusinessByBusiness(BusinessVO businessVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusiness(BusinessVO businessVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Exam> getStatusLog(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

}
