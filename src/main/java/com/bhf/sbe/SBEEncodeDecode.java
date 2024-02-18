package com.bhf.sbe;

import com.bht.md.messages.*;
import com.bht.md.messages.encodehelpers.SingleSidedQuoteEncodeHelper;
import java.nio.ByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class SBEEncodeDecode {

    public static final int CAPACITY = 512;
    public static final int ALIGNMENT = 8;
    SingleSidedQuoteEncoder singleSidedQuoteEncoder = new SingleSidedQuoteEncoder();
    SingleSidedQuoteEncodeHelper singleSidedQuoteEncodeHelper = new SingleSidedQuoteEncodeHelper();
    MessageHeaderEncoder headerEnc = new MessageHeaderEncoder();
    ByteBuffer directAlignedBuffer = org.agrona.BufferUtil.allocateDirectAligned(512, 8);
    MutableDirectBuffer buffer = new org.agrona.concurrent.UnsafeBuffer(directAlignedBuffer);

    SingleSidedQuoteDecoder dec = new SingleSidedQuoteDecoder();
    MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();

    @Setup(Level.Iteration)
    public void setup(){
        final long timeValue = 12923939;
        final short symbolId = 1;
        final QuoteCondition quoteCondition = QuoteCondition.Direct;
        final MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        final long price = 123;
        final long qty = 55;
        final QuoteSide quoteSide = QuoteSide.Ask;

        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder,
                buffer,
                headerEnc,
                timeValue,
                symbolId,
                marketState,
                quoteCondition,
                price,
                qty,
                quoteSide);
    }


    @Benchmark
    public void testEncode(Blackhole bh){
        final long timeValue = 12923939;
        final short symbolId = 1;
        final QuoteCondition quoteCondition = QuoteCondition.Direct;
        final MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        final long price = 123;
        final long qty = 55;
        final QuoteSide quoteSide = QuoteSide.Ask;

        singleSidedQuoteEncodeHelper.encodeSingleSidedQuote(
                singleSidedQuoteEncoder,
                buffer,
                headerEnc,
                timeValue,
                symbolId,
                marketState,
                quoteCondition,
                price,
                qty,
                quoteSide);

        bh.consume(timeValue);
    }

    @Benchmark
    public void testDecode(Blackhole bh){

        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);

        bh.consume(dec.time());
    }

    @Benchmark
    public void testDecodeWithFieldAccess(Blackhole bh){

        dec.wrapAndApplyHeader(buffer, 0, headerDecoder);
        long ts=dec.time();
        long px=dec.quote().price();
        long qty=dec.quote().qty();
        QuoteCondition quoteCondition = dec.quoteCondition();
        MarketState marketState = dec.marketState();

        //bh.consume(ts+px+qty*quoteCondition.hashCode()+marketState.hashCode());
        bh.consume(ts);
    }


}
