package com.youngdatafan.portal.system.management.gateway.service;

import com.datafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.portal.system.management.gateway.MsgType;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteDTO;
import com.youngdatafan.portal.system.management.gateway.dto.DpGatewayRouteMsgDTO;
import com.youngdatafan.portal.system.management.gateway.entity.DpGatewayRoute;
import com.youngdatafan.portal.system.management.gateway.mapper.DpGatewayRouteMapper;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRouteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author gavin
 * @create 2020/6/12 5:20 下午
 */
@Service
public class GatewayRouteService {
    private static final Logger log = LoggerFactory.getLogger(GatewayRouteService.class);

    private final DpGatewayRouteMapper dpGatewayRouteMapper;
    private final RedisPublisherService redisPublisherService;

    @Value("${gateway.route.channel:gateway_route}")
    private String gatewayRouteChannel;

    @Autowired
    public GatewayRouteService(DpGatewayRouteMapper dpGatewayRouteMapper, RedisPublisherService redisPublisherService) {
        this.dpGatewayRouteMapper = dpGatewayRouteMapper;
        this.redisPublisherService = redisPublisherService;
    }

    /**
     * 增加或者更新规则
     *
     * @param dpGatewayRouteVO DpGatewayRouteVO
     */
    public DpGatewayRouteDTO upsert(DpGatewayRouteVO dpGatewayRouteVO) {
        DpGatewayRoute record = new DpGatewayRoute();
        BeanUtils.copyProperties(dpGatewayRouteVO, record);

        if (dpGatewayRouteMapper.selectByPrimaryKey(dpGatewayRouteVO.getId()) != null) {
            record.setUpdateTime(new Date());
            record.setStatus(1);
            dpGatewayRouteMapper.updateByPrimaryKeySelective(record);
        } else {
            record.setCreateTime(new Date());
            record.setStatus(1);
            dpGatewayRouteMapper.insert(record);
        }

        final DpGatewayRouteDTO result = new DpGatewayRouteDTO();
        BeanUtils.copyProperties(record, result);

        // 发送redis消息
        redisPublisherService.pubMsg(gatewayRouteChannel
                , JsonUtils.toString(new DpGatewayRouteMsgDTO(MsgType.UPSET, result)));

        return result;
    }

    public int deleteByPrimaryKey(String id) {
        final int i = dpGatewayRouteMapper.deleteByPrimaryKey(id);
        DpGatewayRouteDTO dpGatewayRouteDTO = new DpGatewayRouteDTO();
        dpGatewayRouteDTO.setId(id);
        dpGatewayRouteDTO.setStatus(1);

        // 发送redis消息
        redisPublisherService.pubMsg(gatewayRouteChannel
                , JsonUtils.toString(new DpGatewayRouteMsgDTO(MsgType.DELETE, dpGatewayRouteDTO)));
        return i;
    }

    public List<DpGatewayRouteDTO> selectAll() {
        return dpGatewayRouteMapper.selectAll();
    }

}
