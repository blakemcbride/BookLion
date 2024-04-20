
package booklion.server.edituser;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import business.BLoginUser;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import beans.LoginUser;
import booklion.client.edituser.EditUserService;
import booklion.client.edituser.PageData;
import booklion.client.edituser.Record;
import booklion.client.utils.StandardReturn;
import business.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import dbutils.ExceptionMessage;
import dbutils.HibernateCriteriaUtil;
import dbutils.HibernateSessionUtil;
import dbutils.HibernateUtil;
import utils.Crypto;

/**
 * @author Blake McBride
 */
public class EditUserServiceImpl extends RemoteServiceServlet implements EditUserService {

	private static final Logger logger = Logger.getLogger(EditUserServiceImpl.class);
	private static final long serialVersionUID = 1L;

	/**
	 * Returns a page full of records.
	 * 
	 * @param uuid
	 * @return
	 * 
	 */
	@Override
	public PagingLoadResult<Record> getPosts(String uuid, PagingLoadConfig config, Record firstRec, Record lastRec, 
	        int numOfRecs, String searchText1, String searchText2) {
		HibernateSessionUtil hsu = null;
		PageData res = null;
		try {
			hsu = HibernateUtil.openHSU();
			User user = new User(uuid);
			if (!user.isValidLogin())
				return null;
			if (!user.isAdministrator()) {
				logger.error("not an admin");
				return null;
			}
			
			int limit = config.getLimit();  //  max number of recs on page
			int offset = config.getOffset();
			res = new PageData();
			HibernateCriteriaUtil<LoginUser> hcu;
			
			//  I was attempting to cache the number of records in the DB to avoid getting the count each time but 
			//  I can't handle adding or deleting records that way
//			if (numOfRecs != 0)
//				res.setTotalLength(numOfRecs);
//			else {
//				hcu = hsu.createCriteria(LoginUser.class);
//				numOfRecs = (int) hcu.numberOfRecords();
//				res.setTotalLength(numOfRecs);
//			}
			
			
			
			String sortField = LoginUser.EMAIL;  // default sort field
			boolean ascending = true;
			List<? extends SortInfo> sortInfo = config.getSortInfo();
			if (!sortInfo.isEmpty()) {
				SortInfo si = sortInfo.get(0);
				if ("email".equals(si.getSortField())) {
					sortField = LoginUser.EMAIL;
					ascending = si.getSortDir().ordinal() == 0;
				} else if ("lname".equals(si.getSortField())  ||  "fname".equals(si.getSortField())) {
					sortField = LoginUser.LNAME;
					ascending = si.getSortDir().ordinal() == 0;
				}
			}

			hcu = hsu.createCriteria(LoginUser.class);
			setSelection(hcu, sortField, searchText1, searchText2);
			numOfRecs = (int) hcu.numberOfRecords();
			res.setTotalLength(numOfRecs);

			int num = offset+limit > numOfRecs ? numOfRecs - offset : limit;  //  actual number of recs on page

			boolean reverse = false;
			List<Record> data = new LinkedList<Record>();
			res.setOffset(offset);
			hcu = hsu.createCriteria(LoginUser.class);
			hcu.setMaxResults(num);
			List<LoginUser> recs;
			
			if (offset == 0  ||  firstRec == null  ||  lastRec == null) { // forward from beginning
				orderBy(hcu, sortField, ascending);
			} else if (offset-1 == lastRec.getRecNo()) {  //  one page forward
				orderBy(hcu, sortField, ascending);
				if (ascending) {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameGTCriteria(lastRec));
					} else
						hcu.gtIgnoreCase(sortField, getSortField(lastRec, sortField));
				} else {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameLTCriteria(lastRec));
					} else
						hcu.ltIgnoreCase(sortField, getSortField(lastRec, sortField));					
				}
			} else if (offset+num == firstRec.getRecNo())  {  //  one page back
				orderBy(hcu, sortField, !ascending);
				if (ascending) {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameLTCriteria(firstRec));
					} else
						hcu.ltIgnoreCase(sortField, getSortField(firstRec, sortField));
				} else {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameGTCriteria(firstRec));
					} else
						hcu.gtIgnoreCase(sortField, getSortField(firstRec, sortField));					
				}
				reverse = true;
			} else if (offset+num == numOfRecs) {  //  last page
				orderBy(hcu, sortField, !ascending);
				reverse = true;
			} else if (firstRec.getRecNo() == offset) {  // page reload (not on the first page)
				orderBy(hcu, sortField, ascending);
				if (ascending) {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameGECriteria(firstRec));
					} else
						hcu.geIgnoreCase(sortField, getSortField(firstRec, sortField));
				} else {
					if (LoginUser.LNAME.equals(sortField)) {
						hcu.add(addNameLECriteria(firstRec));
					} else
						hcu.leIgnoreCase(sortField, getSortField(firstRec, sortField));					
				}
			} else {  //  unknown page operation
				logger.error("unknown orientation");
				hcu = null;
			}
			
			setSelection(hcu, sortField, searchText1, searchText2);
			
			recs = hcu.list();
			for (int i=0 ; i < num ; i++) {
				LoginUser rec = recs.get(reverse ? num-i-1 : i);
				Record p = new Record();
				p.setRecNo(i+offset);
				p.setUserId(rec.getUserId());
				p.setFname(rec.getFname());
				p.setLname(rec.getLname());
				p.setAdmin(rec.getAdministrator());
				p.setBirthDate(rec.getBirthDate());
				p.setEmail(rec.getEmailAddress());
				p.setPassword("");  // don't send, only change if they provide a new one
				p.setSex(rec.getSex().charAt(0));
                p.setNewEmail(rec.getNewEmail());
                p.setNewPassword(""); // don't send, only change if they provide a new one
                p.setUserStatus(rec.getUserStatus());
				data.add(p);
			}

			res.setData(data);
		} catch (Exception e) {
			logger.error(e);
		} finally {
            if (hsu != null)
			    HibernateSessionUtil.close(hsu);
		}
		return res;
	}
	
	private void setSelection(HibernateCriteriaUtil<LoginUser> hcu, String sortField, String  searchText1, String searchText2) {
        hcu.ne(LoginUser.USER_ID, -1L);  // don't show the guest login record
		if (searchText1 != null  &&  !searchText1.isEmpty())
			if (LoginUser.EMAIL.equals(sortField))
				hcu.likeIgnoreCase(LoginUser.EMAIL, searchText1 + "%");
			else {
				hcu.likeIgnoreCase(LoginUser.LNAME, searchText1 + "%");
				if (searchText2 != null  &&  !searchText2.isEmpty())
					hcu.likeIgnoreCase(LoginUser.FNAME, searchText2 + "%");
			}		
	}
	
	private Criterion addNameGTCriteria(Record rec) {
		Criterion c1 = Restrictions.gt(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase();
		Criterion c2 = Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
				Restrictions.gt(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase());
		Criterion c3 = Restrictions.and(
				Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
						         Restrictions.eq(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
				Restrictions.gt(LoginUser.USER_ID, rec.getUserId()));
		Criterion c4 = Restrictions.or(c1,  c2);
		Criterion c5 = Restrictions.or(c4,  c3);
		return c5;
	}
	
	private Criterion addNameGECriteria(Record rec) {
		Criterion c1 = Restrictions.and(
				Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
						         Restrictions.eq(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
				Restrictions.eq(LoginUser.USER_ID, rec.getUserId()));
		Criterion c2 = Restrictions.or(c1, addNameGTCriteria(rec));
		return c2;
	}
	
	private Criterion addNameLTCriteria(Record rec) {
		Criterion c1 = Restrictions.lt(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase();
		Criterion c2 = Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
				Restrictions.lt(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase());
		Criterion c3 = Restrictions.and(
				Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
						         Restrictions.eq(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
				Restrictions.lt(LoginUser.USER_ID, rec.getUserId()));
		Criterion c4 = Restrictions.or(c1,  c2);
		Criterion c5 = Restrictions.or(c4,  c3);
		return c5;
	}
	
	private Criterion addNameLECriteria(Record rec) {
		Criterion c1 = Restrictions.and(
				Restrictions.and(Restrictions.eq(LoginUser.LNAME, rec.getLname().toLowerCase()).ignoreCase(),
						         Restrictions.eq(LoginUser.FNAME, rec.getFname().toLowerCase()).ignoreCase()),
				Restrictions.eq(LoginUser.USER_ID, rec.getUserId()));
		Criterion c2 = Restrictions.or(c1, addNameLTCriteria(rec));
		return c2;
	}
	
	private void orderBy(HibernateCriteriaUtil<LoginUser> hcu, String fld, boolean ascending) {
		if (ascending) {
			hcu.orderByIgnoreCase(fld);
			if (fld.equals(LoginUser.LNAME)) {
				hcu.orderByIgnoreCase(LoginUser.FNAME);
				hcu.orderBy(LoginUser.USER_ID);
			}
		} else {
			hcu.orderByDescIgnoreCase(fld);
			if (fld.equals(LoginUser.LNAME)) {
				hcu.orderByDescIgnoreCase(LoginUser.FNAME);
				hcu.orderBy(LoginUser.USER_ID);
			}
		}
	}
	
	private String getSortField(Record rec, String sortField) {
		if (sortField.equals(LoginUser.EMAIL))
			return rec.getEmail().toLowerCase();
		else if (sortField.equals(LoginUser.LNAME))
			return rec.getLname().toLowerCase();
		return null;
	}

	@Override
	public StandardReturn updateRecord(String uuid, Record rec) {
		HibernateSessionUtil hsu = null;
		StandardReturn res;
		try {
			hsu = HibernateUtil.openHSU();
			User user = new User(uuid);
			if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
			if (!user.isAdministrator())
				return new StandardReturn("Error: action requires supervisor privileges");

            BLoginUser buser = new BLoginUser(rec.getUserId());
            buser.createHistoryModify(user);
			LoginUser updateRec = buser.getBean();
			
			updateRec.setFname(rec.getFname());
			updateRec.setLname(rec.getLname());
			
			String pw = rec.getPassword();
			if (pw != null  &&  pw.length() > 0)  //  if no password passed don't blank out.  Only change if new one passed.
				updateRec.setPassword(Crypto.hashPW(pw));
			updateRec.setEmailAddress(rec.getEmail());
			updateRec.setBirthDate(rec.getBirthDate());
			updateRec.setSex(rec.getSex() + "");
			updateRec.setAdministrator(rec.getAdmin());
            updateRec.setNewEmail(rec.getNewEmail());
            updateRec.setUserStatus(rec.getUserStatus());
            pw = rec.getNewPassword();
            if (pw != null  &&  pw.length() > 0)  //  if no password passed don't blank out.  Only change if new one passed.
                updateRec.setNewPassword(Crypto.hashPW(pw));
			
			hsu.commit();
			
			res = new StandardReturn(StandardReturn.SUCCESS);
		} catch (Exception e) {
			res = ExceptionMessage.getReturn(e);
		} finally {
			HibernateSessionUtil.close(hsu);
		}
		return res;
	}

	@Override
	public StandardReturn addRecord(String uuid, Record rec) {
		HibernateSessionUtil hsu = null;
		StandardReturn res;
		try {
			hsu = HibernateUtil.openHSU();
			User user = new User(uuid);
			if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
			if (!user.isAdministrator())
				return new StandardReturn("Error: action requires supervisor privileges");
			
			BLoginUser newBRec = new BLoginUser(user);
			LoginUser newRec = newBRec.getBean();
			newRec.setFname(rec.getFname());
			newRec.setLname(rec.getLname());
			
			newRec.setPassword(Crypto.hashPW(rec.getPassword()));
			newRec.setEmailAddress(rec.getEmail());
			newRec.setBirthDate(rec.getBirthDate());
			newRec.setSex(rec.getSex() + "");
			newRec.setAdministrator(rec.getAdmin());
			newRec.setWhenAdded(new Timestamp(Calendar.getInstance().getTime().getTime()));
            newRec.setUserStatus("A");
			
			hsu.save(newRec);
			
			hsu.commit();
			
			res = new StandardReturn(StandardReturn.SUCCESS);
		} catch (Exception e) {
			res = ExceptionMessage.getReturn(e);
		} finally {
			HibernateSessionUtil.close(hsu);
		}
		return res;
	}
	
	@Override
	public StandardReturn deleteRecord(String uuid, Record rec) {
		HibernateSessionUtil hsu = null;
		StandardReturn res;
		try {
			hsu = HibernateUtil.openHSU();
			User user = new User(uuid);
			if (!user.isNonGuestValidLogin())
                return user.invalidLogin();
			if (!user.isAdministrator())
				return new StandardReturn("Error: action requires supervisor privileges");

            new BLoginUser(rec.getUserId()).delete(user);
			
			hsu.commit();
			
			res = new StandardReturn(StandardReturn.SUCCESS);
		} catch (Exception e) {
			res = ExceptionMessage.getReturn(e);
		} finally {
			HibernateSessionUtil.close(hsu);
		}
		return res;
	}
}
