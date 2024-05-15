package com.co.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.co.backend.model.po.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}
