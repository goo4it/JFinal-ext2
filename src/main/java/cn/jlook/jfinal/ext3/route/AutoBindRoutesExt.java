/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jlook.jfinal.ext3.route;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

/**
 * 重写AutoBindRoutes，支持自定义ViewPath
 * 
 * @author yanglinghui
 */
public class AutoBindRoutesExt extends AutoBindRoutes {
    private String default_view_path;

    public AutoBindRoutesExt setDefaultViewPath(String default_view_path) {
        this.default_view_path = default_view_path;
        return this;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void config() {
        List<Class<? extends Controller>> controllerClasses = ClassSearcher.of(Controller.class)
                .includeAllJarsInLib(includeAllJarsInLib).injars(includeJars).search();
        ControllerBind controllerBind = null;
        for (Class controller : controllerClasses) {
            if (excludeClasses.contains(controller)) {
                continue;
            }
            controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
            if (controllerBind == null) {
                if (!autoScan) {
                    continue;
                }
                this.add(controllerKey(controller), controller);
                logger.debug("routes.add(" + controllerKey(controller) + ", " + controller.getName() + ")");
            } else if (StrKit.isBlank(controllerBind.viewPath())) {
                if (StrKit.isBlank(default_view_path)) {
                    this.add(controllerBind.controllerKey(), controller);
                    logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller.getName() + ")");
                } else {
                    this.add(controllerBind.controllerKey(), controller,
                            default_view_path + controllerBind.controllerKey());
                    logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller + ","
                            + default_view_path + controllerBind.controllerKey() + ")");
                }
            } else {
                this.add(controllerBind.controllerKey(), controller, controllerBind.viewPath());
                logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller + ","
                        + controllerBind.viewPath() + ")");
            }
        }
    }

}