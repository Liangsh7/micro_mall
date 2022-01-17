import com.alibaba.fastjson.JSON;
import com.mall.promo.PromoService;
import com.mall.promo.bootstrap.PromoProviderApplication;
import com.mall.promo.dto.CreatePromoOrderRequest;
import com.mall.promo.dto.CreatePromoOrderResponse;
import com.mall.promo.dto.PromoProductDetailRequest;
import com.mall.promo.dto.PromoProductDetailResponse;
import org.checkerframework.common.util.report.qual.ReportUnqualified;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@SpringBootTest(classes = PromoProviderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class PromoTest {

    @Autowired
    PromoService promoService;

    @Test
    public void test01(){

        CreatePromoOrderRequest request = new CreatePromoOrderRequest();
        request.setUsername("cskaoyan01");
        request.setUserId(71l);
        request.setPsId(1l);
        request.setProductId(100057401l);
//        CreatePromoOrderResponse createPromoOrderResponse = promoService.savePromoOrderIntransaction(request);

    }

    @Test
    public void test02(){
        PromoProductDetailRequest request = new PromoProductDetailRequest();
        request.setProductId(100052801l);
        request.setPsId(2l);
        PromoProductDetailResponse promoProductProduct = promoService.getPromoProductProduct(request);

        System.out.println(JSON.toJSONString(promoProductProduct));

    }
}