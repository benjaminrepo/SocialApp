package com.social.twitter.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.social.auth.util.SocialUserUtil;
import com.social.twitter.model.SearchCriteria;

public class QueryCriteriaConsumer implements Consumer<SearchCriteria> {
	private Predicate predicate;
	private CriteriaBuilder builder;
	private Root<?> r;

	public QueryCriteriaConsumer(Predicate p, CriteriaBuilder builder, Root<?> r) {
		this.builder = builder;
		this.r = r;
		this.predicate = builder.and(p,
				builder.equal(r.get("user").get("id"), SocialUserUtil.getLoggedInUser().getTUID()));
	}

	@Override
	public void accept(SearchCriteria param) {
		try {
			if (param.getOperation().equalsIgnoreCase(">")) {
				if (r.get(param.getKey()).getJavaType() == Date.class) {
					predicate = builder.and(predicate, builder.greaterThanOrEqualTo(r.get(param.getKey()),
							(Date) new SimpleDateFormat("yyyy-MM-dd").parse(param.getValue().toString())));
				} else {
					predicate = builder.and(predicate,
							builder.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
				}

			} else if (param.getOperation().equalsIgnoreCase("<")) {

				if (r.get(param.getKey()).getJavaType() == Date.class) {
					predicate = builder.and(predicate, builder.lessThanOrEqualTo(r.get(param.getKey()),
							(Date) new SimpleDateFormat("yyyy-MM-dd").parse(param.getValue().toString())));
				} else {
					predicate = builder.and(predicate,
							builder.lessThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
				}

			} else if (param.getOperation().equalsIgnoreCase(":")) {

				if("search".equals(param.getKey())) {
					
					Predicate search = builder.or(builder.like(r.get("text"), "%" + param.getValue() + "%"),
							builder.like(r.get("user").get("name"), "%" + param.getValue() + "%"),
							builder.like(r.get("user").get("screenName"), "%" + param.getValue() + "%"));
					
					predicate = builder.and(predicate, search);
					
				}else if (r.get(param.getKey()).getJavaType() == String.class) {
					
					predicate = builder.and(predicate,
							builder.like(r.get(param.getKey()), "%" + param.getValue() + "%"));
					
				} else if (r.get(param.getKey()).getJavaType() == boolean.class) {
					
					predicate = builder.and(predicate,
							builder.equal(r.get(param.getKey()), new Boolean(param.getValue().toString())));
					
				} else if (r.get(param.getKey()).getJavaType() == Date.class) {

					predicate = builder.and(predicate, builder.equal(r.get(param.getKey()),
							(Date) new SimpleDateFormat("yyyy-MM-dd").parse(param.getValue().toString())));

				} else {
					predicate = builder.and(predicate, builder.equal(r.get(param.getKey()), param.getValue()));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	// Yet to work on this.
	private <T extends Object> T getCovertedObject(SearchCriteria param, Class<T> type) {
		String val = param.getValue().toString();
		if (type == null)
			return (T) val;
		try {
			if (r.get(param.getKey()).getJavaType() == String.class) {
				return type.cast("%" + val + "%");
			} else if (r.get(param.getKey()).getJavaType() == boolean.class) {
				return type.cast(new Boolean(val));
			} else if (r.get(param.getKey()).getJavaType() == Date.class) {
				return type.cast(new SimpleDateFormat("yyyy-MM-dd").parse(val));
			} else {
				return (T) val;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Predicate getPredicate() {
		return predicate;
	}

}
