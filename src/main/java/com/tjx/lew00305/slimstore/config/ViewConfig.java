package com.tjx.lew00305.slimstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.annotation.RequestScope;

import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;

import lombok.RequiredArgsConstructor;

@Configuration
@ImportResource("classpath:view/**/*.xml")
@RequestScope
@RequiredArgsConstructor
public class ViewConfig {

    private final View[] views;

    public View[] getAll() {
        return views;
    }

    public View getView(
        ViewName viewName
    ) {
        View pageNotFound = new View();
        for (View view : views) {
            if (view.getName().equals(viewName)) {
                return view;
            }
            if (view.getName().equals(ViewName.PAGE_NOT_FOUND)) {
                pageNotFound = view;
            }
        }
        return pageNotFound;
    }

}
