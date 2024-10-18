package dev.hoainamtd.repository.specification;

import dev.hoainamtd.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationsBuilder {
    public final List<SpecSearchCriteria> param;

    public UserSpecificationsBuilder() {
        this.param = new ArrayList<>();
    }

    public UserSpecificationsBuilder with (String key, String operation, Object value, String suffix, String prefix) {
        return with ( null, key, operation, value, suffix, prefix);
    }

    public UserSpecificationsBuilder with (String orPredicate, String key, String operation, Object value, String suffix, String prefix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper == SearchOperation.EQUALITY ) {
            boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

            if (startWithAsterisk && endWithAsterisk) {
                oper = SearchOperation.CONTAINS;
            } else if (startWithAsterisk) {
                oper = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                oper = SearchOperation.STARTS_WITH;
            }
        }

        param.add(new SpecSearchCriteria(orPredicate, key, oper, value));

        return this;
    }

    public Specification<User> build() {
        if (param.isEmpty()) return null;

        Specification<User> specification = new UserSpecification(param.get(0));

        // thì từ ở đây chúng ta sẽ nối các điều kiện với khi orPredicate là true sẽ là OR còn false sẽ là AND
        for (int i = 1; i < param.size(); i++) {
            specification = param.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new UserSpecification(param.get(i)))
                    : Specification.where(specification).and(new UserSpecification(param.get(i)));
        }

        return specification;
    }
}
