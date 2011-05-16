package br.usp.choreos.examples.auctionhouse;

import java.math.BigDecimal;
import java.util.HashMap;

public class AuctionHouse {

    private int nextId = 0;

    HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>();

    public int publishAuction(ProductInfo productInfo, BigDecimal startingPrice) throws AuctionHouseException {

	if (productInfo.getHeadline() == null || productInfo.getHeadline().equals(""))
	    throw new AuctionHouseException("invalid headline");
	if (productInfo.getDescription() == null || productInfo.getDescription().equals(""))
	    throw new AuctionHouseException("invalid description");
	if (startingPrice == null || startingPrice.compareTo(BigDecimal.valueOf(0)) == -1)
	    throw new AuctionHouseException("invalid starting price");

	Auction auction = new Auction(productInfo, startingPrice);
	int id = nextId++;
	auctions.put(id, auction);

	return id;
    }

    public void placeOffer(int auctionId, BigDecimal offer) throws AuctionHouseException {

	Auction auction = auctions.get(auctionId);

	if (auction.getCurrentPrice() == null) {
	    // No offers have been placed yet
	    if (auction.getStartingPrice().compareTo(offer) <= 0) {
		auction.setCurrentPrice(offer);
	    } else {
		throw new AuctionHouseException("offer is less than starting price");
	    }
	} else {
	    if (auction.getCurrentPrice().compareTo(offer) == -1) {
		auction.setCurrentPrice(offer);
	    } else {
		throw new AuctionHouseException("offer is less than or equal to current price");
	    }
	}
    }

    private class Auction {
	private BigDecimal startingPrice;
	private BigDecimal currentPrice;
	private ProductInfo productInfo;

	public Auction(ProductInfo productInfo, BigDecimal startingPrice) {
	    this.startingPrice = startingPrice;
	    this.productInfo = productInfo;
	}

	public BigDecimal getStartingPrice() {
	    return startingPrice;
	}

	public ProductInfo getProductInfo() {
	    return productInfo;
	}

	public BigDecimal getCurrentPrice() {
	    return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
	    this.currentPrice = currentPrice;
	}
    }
}