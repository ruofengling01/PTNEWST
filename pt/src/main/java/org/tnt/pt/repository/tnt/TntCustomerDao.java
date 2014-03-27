package org.tnt.pt.repository.tnt;


import org.tnt.pt.repository.BaseDao;
import org.tnt.pt.tntentity.Customer;



/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepositoryTnt
public interface TntCustomerDao extends BaseDao<Customer> {

	Customer findByAccountNo(String accountNo);
	
}
