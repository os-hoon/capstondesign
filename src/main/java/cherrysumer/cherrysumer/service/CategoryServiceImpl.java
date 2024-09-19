package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Category;
import cherrysumer.cherrysumer.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name) {
        // 1. 카테고리 이름이 이미 존재하는지 확인
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다: " + name);
        }

        Category category = new Category();
        category.setName(name); // 카테고리 이름 설정
        return categoryRepository.save(category); // 카테고리 저장
    }
}
