package com.co.codeconnectjudge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.co.codeconnectjudge.model.po.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}
