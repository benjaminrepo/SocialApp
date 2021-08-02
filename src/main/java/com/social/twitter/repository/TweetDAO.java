package com.social.twitter.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.social.twitter.model.SearchCriteria;
import com.social.twitter.model.Tweet;

@Repository
public class TweetDAO {

	@PersistenceContext
    private EntityManager entityManager;
	
    public List<Tweet> searchTweets(final List<SearchCriteria> params, Pageable pageable) {
        try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Tweet> criteriaQuery = criteriaBuilder.createQuery(Tweet.class);
			Root<Tweet> Tweet_table = criteriaQuery.from(Tweet.class);
			//Join<Tweet, TUser> users_table = Tweet_table.join("TUsers", JoinType.INNER);
			
			Predicate predicate = criteriaBuilder.conjunction();

			QueryCriteriaConsumer tweetCrt = 
			  new QueryCriteriaConsumer(predicate, criteriaBuilder, Tweet_table);
			params.stream().forEach(tweetCrt);
			
			Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
			
			if (sort.isSorted())
			   criteriaQuery.orderBy(QueryUtils.toOrders(sort, Tweet_table, criteriaBuilder));
			
			criteriaQuery.distinct(true);
			criteriaQuery.where(predicate);
			
			predicate = tweetCrt.getPredicate();
			criteriaQuery.where(predicate);
 
			
			
			TypedQuery<Tweet> typedQuery = entityManager.createQuery(criteriaQuery);        
			
			//For pagination
			typedQuery.setFirstResult((int) pageable.getOffset());
			typedQuery.setMaxResults(pageable.getPageSize());

			
			
			List<Tweet> result = typedQuery.getResultList();
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
