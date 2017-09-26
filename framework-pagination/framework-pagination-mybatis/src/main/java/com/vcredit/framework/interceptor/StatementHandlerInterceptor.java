package com.vcredit.framework.interceptor;  

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;

import com.vcredit.framework.pagination.Criteria;
import com.vcredit.framework.pagination.DefaultCriteria;



/**
 * 查询自动分页拦截器
 *
 */
@Intercepts({@Signature(type=StatementHandler.class, method="prepare", args={Connection.class})})
public class StatementHandlerInterceptor implements Interceptor {
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();  
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();  
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		Object parameter = statementHandler.getParameterHandler().getParameterObject();
	
		if (parameter instanceof DefaultCriteria) {
			Criteria criteria = (Criteria)parameter;
			Long page = criteria.getPage();
			Long pageSize = criteria.getPageSize();
			String sort = criteria.getSort();
			String order = criteria.getOrder();
			StringBuffer sortBu = new StringBuffer();
			if(sort!=null){
				for(char so:sort.toCharArray()){
					String s = new Character(so).toString();
					if (s.matches("[A-Z]")){
						sortBu.append("_");
					}
					sortBu.append(s.toUpperCase());
				}
			}
			if (page == null || pageSize == null) {
				return invocation.proceed();
			} 
			
			long from = (page-1)*pageSize;
			long to = pageSize;
				
			BoundSql boundSql = statementHandler.getBoundSql();
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			
			int mappingCount = parameterMappings.size();
			
			for (int i = 0; i < mappingCount; i++) {
				parameterMappings.add(parameterMappings.get(i));
			}
			
			String sql = boundSql.getSql();
			StringBuffer orderSql = new StringBuffer();
			StringBuffer pageSql = new StringBuffer();
//			StringBuffer countSql = new StringBuffer();
//			StringBuffer sqlBu=new StringBuffer();
			if(sortBu.length()>0){
				orderSql.append(sql).append(" order by ").append(sortBu.toString());
				if(order!=null)
					orderSql.append(" ").append(order);
			}else{
				orderSql.append(sql);
			}
			pageSql.append("select f_rt.*, (").append("select count(1) from ( ").append(orderSql).append(" ) c_sq" ).append(")").append(" cnt from ").append("(")
			.append(orderSql).append(" limit ").append(from).append(",").append(to).append(") f_rt ");
//			pageSql.append(" )f_t where rownum <=").append(to).append(") where n >= " ).append(from);
//			sqlBu.append("select f_rt.*, (").append(countSql).append(") cnt from (").append(pageSql.toString()).append(") f_rt");
			
			MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,DEFAULT_OBJECT_FACTORY,DEFAULT_OBJECT_WRAPPER_FACTORY);
			metaStatementHandler.setValue("delegate.boundSql.sql", pageSql.toString());
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET );     
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT );				
		}

		return invocation.proceed();    
	}     

	public Object plugin(Object target) {      
		return Plugin.wrap(target, this);    
	}     
		
	public void setProperties(Properties properties) {} 
}