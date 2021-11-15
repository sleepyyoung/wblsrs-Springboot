package com.philpy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Wblsrs {
    private int rank;
    private String title;
    private int hot;
    private String tag;
    private LocalDateTime time;

    public Wblsrs(String title) {
        this.title = title;
    }

    public Wblsrs(int rank, String title, int hot) {
        this.hot = hot;
        this.rank = rank;
        this.title = title;
    }
}
