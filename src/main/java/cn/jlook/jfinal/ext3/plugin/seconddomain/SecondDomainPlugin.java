package cn.jlook.jfinal.ext3.plugin.seconddomain;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import cn.jlook.jfinal.ext3.kit.SecondDomainKit;
import cn.jlook.jfinal.ext3.kit.SqlBuilder;

@SuppressWarnings("rawtypes")
public class SecondDomainPlugin implements IPlugin {
    private Class<? extends Model> modelClass;
    private String                 keyAttr, valueAttr;

    public SecondDomainPlugin(Class<? extends Model> modelClass, String keyAttr, String valueAttr) {
        this.modelClass = modelClass;
        this.keyAttr = keyAttr;
        this.valueAttr = valueAttr;
    }

    @SuppressWarnings({ "unchecked", "serial" })
    protected List<SecondDomain> loadDomaines() {
        final List<Record> result = SqlBuilder.from(modelClass).select();
        return new ArrayList<SecondDomain>() {
            {
                for (Record record : result) {
                    add(new SecondDomain(record.getStr(keyAttr), record.getStr(valueAttr)));
                }
            }
        };
    }

    @Override
    public boolean start() {
        SecondDomainKit.setSecondDomains(loadDomaines());
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

    public static class SecondDomain {
        private String controllerKey;
        private String domainKey;

        public SecondDomain() {}

        public SecondDomain(String domainKey, String controllerKey) {
            this.domainKey = domainKey;
            this.controllerKey = controllerKey;
        }

        public String getControllerKey() {
            return controllerKey;
        }

        public void setControllerKey(String controllerKey) {
            this.controllerKey = controllerKey;
        }

        public String getDomainKey() {
            return domainKey;
        }

        public void setDomainKey(String domainKey) {
            this.domainKey = domainKey;
        }
    }
}
