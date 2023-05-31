package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory; //동적쿼리 생성 위함

    public ItemRepositoryCustomImpl(EntityManager em){ //생성자로 엔티티매니저 객체 사용
        this.queryFactory = new JPAQueryFactory(em);

    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){ //상품판매조건이 전체(null)일경우 null을 리턴 결과값이 null이면 where절에서 해당조건 무시
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus); //상품 판매상태 조건이 null이아닌 판매중or품절이라면 해당조건의 상품만 조회
        //searchDateType의 값에 따라 dateTime의 값을 이전시간의 값으로 세팅후 해당시간이후로 등록된 상품만 조회
        //       ''     이 1m인 경우  ''     의 값을     한달  전으로 세팅후 최근한달 이후로 등록된상품만 조회
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null)
        {
            return null;
        }
        else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }
        else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }
        else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }
        else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){ //searchBy의 값에따라서 상품명에 검색어를 포함하고있는 상품이나 상품 생성자의 아이디에 검색어를 포함하고있는 상품을 조회하도록 조건값 변경

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");

        } else if (StringUtils.equals("createdBy", searchBy)) {

            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        List<Item> content = queryFactory   //queryFactory를 이용해서 쿼리 생성
                .selectFrom(QItem.item) //상품데이터 조회를 위한 QItem의 Item지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //BooleanExpression 반환하는 조건문을 넣어줌   ,단위로 넣어줄경우 and로 인식
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())    //내림차순
                .offset(pageable.getOffset())    //데이터 가져올 시작 인덱스
                .limit(pageable.getPageSize())    //한번에 가지고올수잇는 갯수
                .fetch();      //쿼리반환

        long total= queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);  //조회한 데이터를 Page 클래스의 구현체인 PageImpl객체로 반환

    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(       //QMainItemDto의 생성자에 반환할 값들 담아주기, @QueryProjection을 사용하여 DTO로 바로 조회.
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)                         //iteming과 item을 내부조인
                .where(itemImg.repimgYn.eq("Y"))                       //상품이미지는 대표상품이미지만 불러옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);

    }




}
