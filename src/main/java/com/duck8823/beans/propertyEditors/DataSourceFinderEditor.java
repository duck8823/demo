package com.duck8823.beans.propertyEditors;

import com.duck8823.service.DataSourceService;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by maeda on 2016/01/24.
 */
public class DataSourceFinderEditor extends PropertyEditorSupport {

	public final DataSourceService service;

	private final boolean allowEmpty;

	public DataSourceFinderEditor(DataSourceService service, boolean allowEmpty) {
		this.service = service;
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(this.allowEmpty && !StringUtils.hasText(text)) {
			this.setValue(null);
		} else {
			try {
				this.setValue(service.findById(Long.parseLong(text)).get());
			} catch (Exception e) {
				throw new IllegalArgumentException("Could not parse " + service.getClass().getGenericSuperclass()  + " : " + e);
			}
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : null;
	}
}
