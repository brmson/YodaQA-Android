package in.vesely.eclub.yodaqa.bus;

import com.squareup.otto.Bus;

import org.androidannotations.annotations.EBean;

/**
 * Created by vesely on 2/3/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class OttoBus extends Bus {

}
