package co.uk.mcb.clubwin;

import co.uk.mcb.clubwin.model.ClubWin;
import org.springframework.batch.item.ItemProcessor;

public class ClubWinItemProcessor implements ItemProcessor<ClubWin, ClubWin> {


    @Override
    public ClubWin process(final ClubWin clubWin) throws Exception {
        return clubWin;
    }
}
