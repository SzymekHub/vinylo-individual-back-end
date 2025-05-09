// package s3.individual.vinylo.controllers;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import s3.individual.vinylo.domain.Artist;
// import s3.individual.vinylo.domain.Auction;
// import s3.individual.vinylo.domain.User;
// import s3.individual.vinylo.domain.Vinyl;
// import s3.individual.vinylo.persistence.entity.RoleEnum;
// import s3.individual.vinylo.persistence.entity.SpeedEnum;
// import s3.individual.vinylo.persistence.entity.StateEnum;
// import s3.individual.vinylo.persistence.entity.VinylColorEnum;
// import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
// import s3.individual.vinylo.services.AuctionService;
// import s3.individual.vinylo.services.UserService;
// import s3.individual.vinylo.services.VinylService;

// import java.time.LocalDate;
// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AuctionControllerTest {

// @Autowired
// private MockMvc mockMvc;

// @MockBean
// private AuctionService auctionService;

// @MockBean
// private VinylService vinylService;

// @MockBean
// private UserService userService;

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testAddAuction_shouldReturn201_whenValidRequest() throws Exception {

// // Arrange
// Auction createdAuction = Auction.builder()
// .id(100)
// .title("Auction Title")
// .vinyl(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_45, "testVinyl",
// "This is a desc for a test", StateEnum.ORIGINAL, VinylColorEnum.BLACK,
// false,
// new Artist(1, "The Beatles", "Famous British rock band"),
// "0ETFjACtuP2ADo6LFhL6HN"))
// .seller(new User(1, "userName", "User@gmail.com", "Password",
// RoleEnum.REGULAR))
// .description("Description")
// .startingPrice(100.00)
// .currentPrice(100.00)
// .startTime(LocalDate.now())
// .endTime(LocalDate.now().plusDays(7))
// .build();

// when(auctionService.saveAuction(eq(null),
// any(Auction.class))).thenReturn(createdAuction);

// when(vinylService.getVinylById(1))
// .thenReturn(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_45,
// "testVinyl",
// "This is a desc for a test", StateEnum.ORIGINAL, VinylColorEnum.BLACK,
// false,
// new Artist(1, "The Beatles", "Famous British rock band"),
// "0ETFjACtuP2ADo6LFhL6HN"));

// when(userService.getUserById(1))
// .thenReturn(new User(1, "userName", "User@gmail.com", "Password",
// RoleEnum.REGULAR));

// // Act
// String startTime = LocalDate.now().toString();
// String endTime = LocalDate.now().plusDays(7).toString();

// String jsonContent = String.format("""
// {
// "title": "Auction Title",
// "vinyl_id": 1,
// "seller_id": 1,
// "description": "Description",
// "startingPrice": 100.00,
// "currentPrice": 100.00,
// "startTime": "%s",
// "endTime": "%s"
// }
// """, startTime, endTime);

// mockMvc.perform(post("/auctions")
// .contentType(APPLICATION_JSON_VALUE)
// .content(jsonContent))
// .andDo(print())
// .andExpect(status().isCreated())
// .andExpect(content().string("Auction was created successfully"));

// // Assert
// verify(auctionService).saveAuction(eq(null), any(Auction.class));
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testAddAuction_shouldReturn404_whenVinylNotFound() throws Exception {

// // Arrange
// when(vinylService.getVinylById(1)).thenReturn(null);

// String startTime = LocalDate.now().toString();
// String endTime = LocalDate.now().plusDays(7).toString();

// String jsonContent = String.format("""
// {
// "title": "Auction Title",
// "vinyl_id": 1,
// "seller_id": 1,
// "description": "Description",
// "startingPrice": 100.00,
// "currentPrice": 100.00,
// "startTime": "%s",
// "endTime": "%s"
// }
// """, startTime, endTime);

// // Assert
// mockMvc.perform(post("/auctions")
// .contentType(APPLICATION_JSON_VALUE)
// .content(jsonContent))
// .andDo(print())
// .andExpect(status().isNotFound())
// .andExpect(content().string("Vinyl with ID 1 not found."));

// verifyNoInteractions(auctionService);
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testAddAuction_shouldReturn404_whenSellerNotFound() throws Exception {

// // Arrange
// when(vinylService.getVinylById(1))
// .thenReturn(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_33_1_3,
// "testVinyl",
// "This is a desc for a test", StateEnum.REMASTERED,
// VinylColorEnum.COLORED, false,
// new Artist(1, "The Beatles", "Famous British rock band"),
// "0ETFjACtuP2ADo6LFhL6HN"));

// int nonExistentSellerId = 888;

// when(userService.getUserById(nonExistentSellerId)).thenReturn(null);

// String startTime = LocalDate.now().toString();
// String endTime = LocalDate.now().plusDays(7).toString();

// String jsonContent = String.format("""
// {
// "title": "Auction Title",
// "vinyl_id": 1,
// "seller_id": 888,
// "description": "Description",
// "startingPrice": 100.00,
// "currentPrice": 100.00,
// "startTime": "%s",
// "endTime": "%s"
// }
// """, startTime, endTime);

// // Assert
// mockMvc.perform(post("/auctions")
// .contentType(APPLICATION_JSON_VALUE)
// .content(jsonContent))
// .andDo(print())
// .andExpect(status().isNotFound())
// .andExpect(content().string("Seller with ID " + nonExistentSellerId + " not
// found."));

// verifyNoInteractions(auctionService);
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testGetAuctions_shouldReturn200AndAuctions() throws Exception {

// // Arrange
// List<Auction> auctions = List.of(
// Auction.builder()
// .id(1)
// .title("Auction1")
// .vinyl(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_33_1_3,
// "testVinyl",
// "This is a desc for a test", StateEnum.REMASTERED,
// VinylColorEnum.COLORED, false,
// new Artist(1, "The Beatles",
// "Famous British rock band"),
// "0ETFjACtuP2ADo6LFhL6HN"))
// .seller(new User(1, "userName", "User@gmail.com", "Password",
// RoleEnum.REGULAR))
// .description("Auction1 Description")
// .startingPrice(69.00)
// .currentPrice(90.00)
// .startTime(LocalDate.now())
// .endTime(LocalDate.now().plusDays(9))
// .build(),

// Auction.builder()
// .id(2)
// .title("Auction2")
// .vinyl(new Vinyl(2, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_33_1_3,
// "testVinyl2",
// "This is a desc for a test2", StateEnum.REMASTERED,
// VinylColorEnum.COLORED, false,
// new Artist(2, "The CockRoaches",
// "Famous American techno band"),
// "0ETFjACtuP2ADo6LFhL6HN"))
// .seller(new User(1, "userName", "User@gmail.com", "Password",
// RoleEnum.REGULAR))
// .description("Auction2 Description")
// .startingPrice(54.00)
// .currentPrice(72.00)
// .startTime(LocalDate.now())
// .endTime(LocalDate.now().plusDays(3))
// .build());

// when(auctionService.getAuctions(0, 5)).thenReturn(auctions);

// // Act and Assert
// mockMvc.perform(get("/auctions"))
// .andDo(print())
// .andExpect(status().isOk())
// .andExpect(content().contentType(APPLICATION_JSON_VALUE))
// .andExpect(jsonPath("$.auctions[0].id").value(1))
// .andExpect(jsonPath("$.auctions[0].title").value("Auction1"))
// .andExpect(jsonPath("$.auctions[1].id").value(2))
// .andExpect(jsonPath("$.auctions[1].title").value("Auction2"));

// verify(auctionService).getAuctions(0, 5);
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testGetAuction_shouldReturn200_whenAuctionExists() throws Exception {
// // Arrange
// int auctionId = 1;
// Auction auction = Auction.builder()
// .id(auctionId)
// .title("Auction1")
// .vinyl(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_33_1_3,
// "testVinyl",
// "This is a desc for a test", StateEnum.REMASTERED,
// VinylColorEnum.COLORED, false,
// new Artist(1, "The Beatles", "Famous British rock band"),
// "0ETFjACtuP2ADo6LFhL6HN"))
// .seller(new User(1, "userName", "User@gmail.com", "Password",
// RoleEnum.REGULAR))
// .description("Auction1 Description")
// .startingPrice(69.00)
// .currentPrice(90.00)
// .startTime(LocalDate.now())
// .endTime(LocalDate.now().plusDays(9))
// .build();

// when(auctionService.getAuctionsById(1)).thenReturn(auction);

// // Act and Assert
// mockMvc.perform(get("/auctions/{id}", auctionId))
// .andDo(print())
// .andExpect(status().isOk())
// .andExpect(content().contentType(APPLICATION_JSON_VALUE))
// .andExpect(jsonPath("$.id").value(auctionId))
// .andExpect(jsonPath("$.title").value("Auction1"))
// .andExpect(jsonPath("$.description").value("Auction1 Description"));

// verify(auctionService).getAuctionsById(auctionId);
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testGetAuction_shouldReturn404_whenAuctionNotFound() throws Exception {
// // Arrange
// int auctionId = 666;
// when(auctionService.getAuctionsById(auctionId)).thenReturn(null);

// // Act and Assert
// mockMvc.perform(get("/auctions/{id}", auctionId))
// .andDo(print())
// .andExpect(status().isNotFound())
// .andExpect(content().string("Auction record not found"));

// verify(auctionService).getAuctionsById(auctionId);
// }

// // @Test
// // @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// // void testReplaceAuctionDescription_shouldReturn200_whenSuccessful() throws
// // Exception {
// // // Arrange
// // int auctionId = 1;
// // Auction existingAuction = Auction.builder()
// // .id(auctionId)
// // .title("Original Auction")
// // .description("Original Description")
// // .vinyl(new Vinyl(1, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_33_1_3,
// // "testVinyl",
// // "This is a desc for a test", StateEnum.REMASTERED,
// // VinylColorEnum.COLORED, false,
// // new Artist(1, "The Beatles", "Famous British rock band"),
// // "0ETFjACtuP2ADo6LFhL6HN"))
// // .seller(new User(1, "userName", "User@gmail.com", "Password",
// // RoleEnum.REGULAR))
// // .startingPrice(10.0)
// // .currentPrice(15.0)
// // .startTime(LocalDate.now())
// // .endTime(LocalDate.now().plusDays(7))
// // .build();

// // Auction updatedAuction = Auction.builder()
// // .id(auctionId)
// // .description("Updated Description")
// // .vinyl(existingAuction.getVinyl())
// // .seller(existingAuction.getSeller())
// // .startingPrice(existingAuction.getStartingPrice())
// // .currentPrice(existingAuction.getCurrentPrice())
// // .startTime(existingAuction.getStartTime())
// // .endTime(existingAuction.getEndTime())
// // .build();

// // when(auctionService.saveAuction(eq(auctionId),
// // any(Auction.class))).thenReturn(updatedAuction);

// // String jsonContent = """
// // {
// // "id": 1,
// // "description": "Updated Description",
// // "vinyl_id": 1,
// // "seller_id": 1,
// // "currentPrice": 15.0
// // }
// // """;

// // // Act and Assert
// // mockMvc.perform(put("/auctions/{id}", auctionId)
// // .contentType(APPLICATION_JSON_VALUE)
// // .content(jsonContent))
// // .andDo(print())
// // .andExpect(status().isOk())
// // .andExpect(jsonPath("$.description").value("Updated Description"));

// // verify(auctionService).saveAuction(eq(auctionId), any(Auction.class));
// // }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testDeactivateAuctionById_shouldReturn200_whenDeleted() throws Exception
// {
// // Arrange
// int auctionId = 100;
// when(auctionService.deativateAuctionById(auctionId)).thenReturn(true);

// // Act and Assert
// mockMvc.perform(delete("/auctions/{id}", auctionId))
// .andDo(print())
// .andExpect(status().isOk())
// .andExpect(content()
// .string("Auction with id " + auctionId + " was successfully deleted."));

// verify(auctionService).deativateAuctionById(auctionId);
// }

// @Test
// @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
// void testDeactivateAuctionById_shouldReturn404_whenNotFound() throws
// Exception {
// // Arrange
// int auctionId = 100;
// when(auctionService.deativateAuctionById(auctionId)).thenReturn(false);

// // Act and Assert
// mockMvc.perform(delete("/auctions/{id}", auctionId))
// .andDo(print())
// .andExpect(status().isNotFound())
// .andExpect(content().string("Auction with id " + auctionId + " was not
// found."));

// verify(auctionService).deativateAuctionById(auctionId);
// }
// }
