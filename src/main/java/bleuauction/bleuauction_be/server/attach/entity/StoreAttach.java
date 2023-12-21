package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("STORE")
public class StoreAttach extends Attach {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_no")
    private Store store;

    public StoreAttach(AttachVO attachVO) {
        super(attachVO);
    }
    public StoreAttach(AttachVO attachVO, Store store) {
        this(attachVO);
        this.store = store;
    }

    public void setStore(Store store) {
        this.store = store;
        store.getAttaches().add(this);
    }
}
