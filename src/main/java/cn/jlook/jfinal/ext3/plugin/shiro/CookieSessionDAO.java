package cn.jlook.jfinal.ext3.plugin.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class CookieSessionDAO extends AbstractSessionDAO {
    private static final Log                     log = Log.getLog(CookieSessionDAO.class);
    private ConcurrentMap<Serializable, Session> sessions;

    public CookieSessionDAO() {
        this.sessions = new ConcurrentHashMap<Serializable, Session>();
    }

    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(sessionId, session);
        return sessionId;
    }

    protected Session storeSession(Serializable id, Session session) {
        log.info("storeSession -> " + session.getId());
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        return sessions.putIfAbsent(id, session);
    }

    protected Session doReadSession(Serializable sessionId) {
        log.info("doReadSession -> " + sessionId);
        return sessions.get(sessionId);
    }

    public void update(Session session) throws UnknownSessionException {
        log.info("update -> " + session.getId());
        storeSession(session.getId(), session);
    }

    public void delete(Session session) {
        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
        log.info("delete -> " + session.getId());
        Serializable id = session.getId();
        if (id != null) {
            sessions.remove(id);
        }
    }

    public Collection<Session> getActiveSessions() {
        Collection<Session> values = sessions.values();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableCollection(values);
        }
    }

}
