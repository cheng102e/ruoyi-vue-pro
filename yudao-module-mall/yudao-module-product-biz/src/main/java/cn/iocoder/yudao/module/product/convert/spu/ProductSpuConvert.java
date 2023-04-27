package cn.iocoder.yudao.module.product.convert.spu;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.value.ProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.*;
import cn.iocoder.yudao.module.product.controller.app.property.vo.value.AppProductPropertyValueDetailRespVO;
import cn.iocoder.yudao.module.product.controller.app.spu.vo.AppProductSpuDetailRespVO;
import cn.iocoder.yudao.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.yudao.module.product.controller.app.spu.vo.AppProductSpuPageItemRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.service.property.bo.ProductPropertyValueDetailRespBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.ObjectUtil.defaultIfNull;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 商品 SPU Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductSpuConvert {

    ProductSpuConvert INSTANCE = Mappers.getMapper(ProductSpuConvert.class);

    ProductSpuDO convertForGetSpuDetail(ProductSpuCreateReqVO bean);

    ProductSpuDO convertForGetSpuDetail(ProductSpuUpdateReqVO bean);

    List<ProductSpuDO> convertList(List<ProductSpuDO> list);

    PageResult<ProductSpuRespVO> convertPage(PageResult<ProductSpuDO> page);

    List<ProductSpuRespDTO> convertList2(List<ProductSpuDO> list);

    List<ProductSpuSimpleRespVO> convertList02(List<ProductSpuDO> list);


    default ProductSpuDetailRespVO convert03(ProductSpuDO spu, List<ProductSkuDO> skus,
                                             List<ProductPropertyValueDetailRespBO> propertyValues) {
        ProductSpuDetailRespVO spuVO = convert03(spu);
        spuVO.setSkus(convertList04(skus));
        // 处理商品属性
        Map<Long, ProductPropertyValueDetailRespBO> propertyValueMap = convertMap(propertyValues, ProductPropertyValueDetailRespBO::getValueId);
        for (int i = 0; i < skus.size(); i++) {
            List<ProductSkuDO.Property> properties = skus.get(i).getProperties();
            if (CollUtil.isEmpty(properties)) {
                continue;
            }
            ProductSpuDetailRespVO.Sku sku = spuVO.getSkus().get(i);
            sku.setProperties(new ArrayList<>(properties.size()));
            // 遍历每个 properties，设置到 AppSpuDetailRespVO.Sku 中
            properties.forEach(property -> {
                ProductPropertyValueDetailRespBO propertyValue = propertyValueMap.get(property.getValueId());
                if (propertyValue == null) {
                    return;
                }
                sku.getProperties().add(convert04(propertyValue));
            });
        }
        return spuVO;
    }
    ProductSpuDetailRespVO convert03(ProductSpuDO spu);
    List<ProductSpuDetailRespVO.Sku> convertList04(List<ProductSkuDO> skus);
    ProductPropertyValueDetailRespVO convert04(ProductPropertyValueDetailRespBO propertyValue);

    // ========== 用户 App 相关 ==========

    default PageResult<AppProductSpuPageItemRespVO> convertPageForGetSpuPage(PageResult<ProductSpuDO> page) {
        // 累加虚拟销量
        page.getList().forEach(spu -> spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount()));
        // 然后进行转换
        return convertPageForGetSpuPage0(page);
    }
    PageResult<AppProductSpuPageItemRespVO> convertPageForGetSpuPage0(PageResult<ProductSpuDO> page);

    default AppProductSpuDetailRespVO convertForGetSpuDetail(ProductSpuDO spu, List<ProductSkuDO> skus,
                                                             List<ProductPropertyValueDetailRespBO> propertyValues) {
        AppProductSpuDetailRespVO spuVO = convertForGetSpuDetail(spu)
                .setSalesCount(spu.getSalesCount() + defaultIfNull(spu.getVirtualSalesCount(), 0));
        spuVO.setSkus(convertListForGetSpuDetail(skus));
        // 处理商品属性
        Map<Long, ProductPropertyValueDetailRespBO> propertyValueMap = convertMap(propertyValues, ProductPropertyValueDetailRespBO::getValueId);
        for (int i = 0; i < skus.size(); i++) {
            List<ProductSkuDO.Property> properties = skus.get(i).getProperties();
            if (CollUtil.isEmpty(properties)) {
                continue;
            }
            AppProductSpuDetailRespVO.Sku sku = spuVO.getSkus().get(i);
            sku.setProperties(new ArrayList<>(properties.size()));
            // 遍历每个 properties，设置到 AppSpuDetailRespVO.Sku 中
            properties.forEach(property -> {
                ProductPropertyValueDetailRespBO propertyValue = propertyValueMap.get(property.getValueId());
                if (propertyValue == null) {
                    return;
                }
                sku.getProperties().add(convertForGetSpuDetail(propertyValue));
            });
        }
        return spuVO;
    }
    AppProductSpuDetailRespVO convertForGetSpuDetail(ProductSpuDO spu);
    List<AppProductSpuDetailRespVO.Sku> convertListForGetSpuDetail(List<ProductSkuDO> skus);
    AppProductPropertyValueDetailRespVO convertForGetSpuDetail(ProductPropertyValueDetailRespBO propertyValue);

}
