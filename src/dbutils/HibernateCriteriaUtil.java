
package dbutils;

import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.*;

public class HibernateCriteriaUtil<T> {
	private Criteria crit;

	public HibernateCriteriaUtil(HibernateSessionUtil hsu, Class<?> c) {
		this.crit = hsu.getSession().createCriteria(c);		
	}
	
	public HibernateCriteriaUtil(Session s, Class<T> clazz) {
		this.crit = s.createCriteria(clazz);
	}

    public HibernateCriteriaUtil<T> eq(String fld, Object value) {
        Criterion c = Restrictions.eq(fld, value);
        crit.add(c);
        return this;
    }

    public HibernateCriteriaUtil<T> ne(String fld, Object value) {
        Criterion c = Restrictions.ne(fld, value);
        crit.add(c);
        return this;
    }

	public HibernateCriteriaUtil<T> eqIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.eq(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> gt(String fld, Object value) {
		Criterion c = Restrictions.gt(fld, value);
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> gtIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.gt(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> ge(String fld, Object value) {
		Criterion c = Restrictions.ge(fld, value);
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> geIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.ge(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> lt(String fld, Object value) {
		Criterion c = Restrictions.lt(fld, value);
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> ltIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.lt(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> le(String fld, Object value) {
		Criterion c = Restrictions.le(fld, value);
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> like(String fld, Object value) {
		Criterion c = Restrictions.like(fld, value);
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> likeIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.like(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> leIgnoreCase(String fld, Object value) {
		Criterion c = Restrictions.le(fld, value).ignoreCase();
		crit.add(c);
		return this;
	}

	public HibernateCriteriaUtil<T> add(Criterion c) {
		crit.add(c);
		return this;
	}

	public ScrollableResults scroll() {
		return crit.scroll();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> list() {
		return crit.list();
	}
	
	public Object getFirst() {
		crit.setMaxResults(1);
		@SuppressWarnings("rawtypes")
		List rows = crit.list();
		if (rows.size() < 1)
			return null;
		return rows.get(0);
	}
	
	public HibernateCriteriaUtil<T> in(String field, Collection<?> collection) {
        if (collection.isEmpty()) {
            // cause it to select nothing
            crit.add(Restrictions.isNotNull(field));
            crit.add(Restrictions.isNull(field));
        } else
		    crit.add(Restrictions.in(field, collection));
		return this;
	}
	
	public void deleteAll() {
		for(Object o : crit.list())
			HibernateUtil.getHSU().getSession().delete(o);
	}
	
	public HibernateCriteriaUtil<T> orderBy(String column) {
		Order order = Order.asc(column);
		crit.addOrder(order);
		return this;
	}
	
	public HibernateCriteriaUtil<T> orderByIgnoreCase(String column) {
		Order order = Order.asc(column).ignoreCase();
		crit.addOrder(order);
		return this;
	}
	
	public HibernateCriteriaUtil<T> orderByDesc(String column) {
		Order order = Order.desc(column);
		crit.addOrder(order);
		return this;
	}
	
	public HibernateCriteriaUtil<T> orderByDescIgnoreCase(String column) {
		Order order = Order.desc(column).ignoreCase();
		crit.addOrder(order);
		return this;
	}
	
	final public HibernateCriteriaUtil<T> setMaxResults(int max) {
		crit.setMaxResults(max);
		return this;
	}

    final public HibernateCriteriaUtil<T> selectColumns(String ... cols) {
        ProjectionList pl = Projections.projectionList();
        for (String col : cols)
            pl.add(Projections.property(col));
        crit.setProjection(pl);
        return this;
    }

    final public long numberOfRecords() {
		crit.setProjection(Projections.rowCount());
		long res = (Long) crit.uniqueResult();
		crit = null;  //  don't use again
		return res;
	}

}
