package com.landanurm.partner_companies_info_provider.simple_array_adapter;

/**
 * Created by Leonid on 03.12.13.
 */
public interface ItemToTitleConvertor<T> {
    String getTitle(T item);
}
