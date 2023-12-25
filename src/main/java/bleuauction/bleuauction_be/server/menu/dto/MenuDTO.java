package bleuauction.bleuauction_be.server.menu.dto;

import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private String name;
    private MenuSize size;
    private int price;
    private String content;
}
