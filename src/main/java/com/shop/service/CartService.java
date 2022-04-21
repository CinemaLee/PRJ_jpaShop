package com.shop.service;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    /**
     * 장바구니에 담기
     * @param cartItemDto
     * @param name
     * @return
     */
    public Long addCart(CartItemDto cartItemDto, String name){

        // 장바구니에 담을 Item 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 현재 로그인한 회원 Entity 조회
        Member member = memberRepository.findByName(name);

        // 현재 로그인한 회원의 장바구니 Entity
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 현재 상품이 장바구니에 이미 들어가 있는가?
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }else{
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }

    }

    /**
     * 장바구니 목록 조회
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String name){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByName(name);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    /**
     * 장바구니 아이템 유효성체크
     * @param cartItemId
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String name){
        Member curMember = memberRepository.findByName(name);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getName(), savedMember.getName())){
            return false;
        }

        return true;
    }

    /**
     * 장바구니 상품 수량 업데이트
     * @param cartItemId
     * @param count
     */
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    /**
     * 장바구니 상품 제거
     * @param cartItemId
     */
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    /**
     * 장바구니에서 주문하기
     * @param cartOrderDtoList
     * @param name
     * @return
     */
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String name){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        // 주문 생성
        Long orderId = orderService.createOrders(orderDtoList, name);

        // 주문한 상품은 장바구니에서 제거
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;

    }
}
