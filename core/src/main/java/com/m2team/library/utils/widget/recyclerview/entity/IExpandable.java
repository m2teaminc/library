package com.m2team.library.utils.widget.recyclerview.entity;

import java.util.List;

public interface IExpandable<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    List<T> getSubItems();

    int getLevel();
}
