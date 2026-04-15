# convention

file name : PascalCase

class name : PascalCase

function name : camelCase

variable name : camelCase

constant name : UPPER_SNAKE_CASE

folder name : lowercase

# folder structure
Feature-based folder structure. Each feature follows this pattern:

    /account
    ├── /constant               # Enums and constants (e.g., AccountStatus.java, AccountType.java, CreditRank.java, Gender.java)
    ├── /controller             # REST controllers (e.g., AccountController.java, BusinessAccountController.java)
    ├── /dto                    # Data Transfer Objects
    │   ├── CreateXxxRequest    # Request DTOs for create operations (abstract base + subclass per account type)
    │   ├── UpdateXxxRequest    # Request DTOs for update operations
    │   └── GetXxxResponse      # Response DTOs (abstract base + subclass per account type)
    ├── /entity                 # JPA entities (e.g., Account.java, PersonalAccount.java, BusinessAccount.java)
    ├── /mapper                 # MapStruct mappers (e.g., AccountMapper.java) — interface annotated with @Mapper
    ├── /repository             # Spring Data JPA repositories (e.g., AccountRepository.java)
    ├── /service
    │   ├── /domain             # Write/command services (e.g., AccountService.java) — annotated with @Service, use @Transactional
    │   └── /query              # Read/query services (e.g., AccountQueryService.java) — use @Transactional(readOnly = true)
    └── /validator              # Business rule validators (e.g., AccountValidator.java) — annotated with @Component

# patterns & conventions

    - Inheritance pattern: abstract base class (Account, CreateAccountRequest, GetAccountResponse) with concrete subclasses per type (Personal, Business, Government)
    - DTOs are pure data classes using Lombok @Data; entities use @Data + @Entity + @NoArgsConstructor
    - Mapper layer uses MapStruct (@Mapper(componentModel = "spring")), never map manually in service/controller
    - Services are split into domain (write) and query (read) if the service is too large; controllers depend on both
    - Validators are @Component classes injected into services; they throw exceptions on invalid input
    - Repository layer is always a Spring Data JPA interface extending JpaRepository and prioritizes method naming conventions for queries of jpa
    - Constants/Enums go in /constant, never inline in entities or DTOs
    - Use @Transactional on write methods; @Transactional(readOnly = true) on read methods
    - Use Lombok (@Data, @RequiredArgsConstructor, @NoArgsConstructor) consistently; avoid writing boilerplate manually
    - Exception handling: throw custom exceptions (e.g., NotFoundException, ForbiddenException) from the common/exception package
    - JWT utility (JwtUtil) is used inside domain services to resolve the current authenticated user (getUsername())

# principle

    - Only allow to change the code that I allow
    - strictly follow the coding convention
    - if you think you have better solution, discuss with me before you implement it
    - if you need more context information about the project, ask me before you implement it
    - ask me if there are some cases that I haven't instructed you to do
    - give the documents link where you found the solution if you are not sure about the solution you found
    - never change unrelated code while implementing a feature
    - always validate your changes with the existing patterns in the codebase before finalizing
    - base on sample code to implement the feature, and make sure your code is consistent with the existing codebase
    - read docs in docs folder before implement to understand the business rules and requirements
    - when write test make sure to use the testcases in testcases file , and assure the tests pass if you try 3 times but still fail, stop and tell me I will tell you why it's wrong 

# commit
    - Use Conventional Commits (feat:, fix:, chore:, refactor: , docs: , ...).
    - Write simple, clear and concise commit messages that describe the changes made.
    - if there is conflict tell me before you resolve it, and explain the reason why you think your solution is the best one.

# instructions 

    - when write tests:
        - 1 follow the principle of instruction file and read code sample
        - 2 read other test and follow its pattern
        - 3 create 2 case for each test (failure and success)

        workflow when write tests:
            - 1: write testcases (make sure testcases correct and pass the validation layer of dto)
            - 2 : unit test for functions in service
            - 3 : integration test using other controller + testcases to create a complete environment , call the api the need to test , 
                after that assert the result(message if api return message, data if api return data) ,
            - 4 :  with tests that do not use @Transaction or @Transactional not support clean up(delete all created data) after the assertion



# sample code

    sample of a full flow query from controller to service to repository:
    query service method example:
      public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );
        }

    domain service method example:
      @Transactional(readOnly = true)
        public GetAccountResponse get() {
            String username = jwtUtil.getUsername();
            Account account = accountQueryService.findByUsername(username);
    
            return accountMapper.toDto(account);
        }

    controller method example:
        @GetMapping
        public ResponseEntity<ResponseDto<GetAccountResponse>> get() {
            GetAccountResponse Response = accountService.get();
            return ResponseEntity.ok(ResponseDto.success(Response, "Account retrieved successfully"));
        }

# pagination sample

    sample of paginated query from controller to service to repository:
    repository method example:
      Page<Card> findByAccount_Username(String username, Pageable pageable);

    query service method example:
      public Page<Card> findByUsernameWithPagination(String username, int page, int limit) {
          return cardRepository.findByAccount_Username(username, PageRequest.of(page, limit));
      }

    domain service method example:
      @Transactional(readOnly = true)
      public List<? extends GetCardResponse> getAllCardByJwtWithPagination(int page, int limit) {
          String username = jwtUtil.getUsername();
          Page<Card> cardPage = cardQueryService.findByUsernameWithPagination(username, page, limit);

          List<CardDetails> cardDetailsList = cardPage.getContent().stream()
                  .map(Card::getCardDetails)
                  .toList();

          return cardMapper.toDtoList(cardDetailsList);
      }

    controller method example:
      @GetMapping(params = {"page", "limit"})
      public ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> getAllFromByJwtWithPagination(
              @RequestParam int page,
              @RequestParam int limit
      ){
          List<? extends GetCardResponse> response = cardService.getAllCardByJwtWithPagination(page, limit);
          return ResponseEntity.ok(ResponseDto.success(response, "Cards retrieved successfully"));
      }

# create and update sample

    sample of create and update flow from controller to service to repository:
    mapper method example:
      @Mapper(componentModel = "spring", imports = Locale.class)
      public interface CardPrivilegeCodeMapper {
          @Mapping(target = "id", ignore = true)
          @Mapping(target = "code", expression = "java(request.getCode() == null ? null : request.getCode().toUpperCase(Locale.ROOT))")
          CardPrivilegeCode toEntity(CreateCardPrivilegeCodeRequest request);
      }

    repository methods example:
      boolean existsByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
              String code,
              LocalDate effectiveTo,
              LocalDate effectiveFrom
      );
      boolean existsByCodeAndIdNotAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
              String code,
              Long excludeId,
              LocalDate effectiveTo,
              LocalDate effectiveFrom
      );
      Optional<CardPrivilegeCode> findByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
              String code,
              LocalDate from,
              LocalDate to
      );

      default boolean hasOverlap(String code, LocalDate effectiveFrom, LocalDate effectiveTo) {
          return existsByCodeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                  code, effectiveTo, effectiveFrom
          );
      }

    domain service create method example:
      @Transactional
      public CardPrivilegeCode create(CreateCardPrivilegeCodeRequest request) {
          CardPrivilegeCode cardPrivilegeCode = cardPrivilegeCodeMapper.toEntity(request);
          cardPrivilegeCodeValidator.validateCreate(cardPrivilegeCode);
          return cardPrivilegeCodeQueryService.save(cardPrivilegeCode);
      }

    domain service update method example:
      @Transactional
      public CardPrivilegeCode update(UpdateCardPrivilegeCodeRequest request) {
          String normalizedCode = request.getCode().toUpperCase(Locale.ROOT);
          CardPrivilegeCode existingCardPrivilegeCode = cardPrivilegeCodeQueryService.findByAccountTypeAndIsActive(normalizedCode);

          cardPrivilegeCodeValidator.validateUpdate(request, existingCardPrivilegeCode);

          return cardPrivilegeCodeQueryService.save(existingCardPrivilegeCode);
      }

    validator update method example:
      public void validateUpdate(UpdateCardPrivilegeCodeRequest request, CardPrivilegeCode existingCardPrivilegeCode) {
          // validate update payload, overlap, and apply non-null fields into existing entity
      }

    controller methods example:
      @PostMapping
      public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateCardPrivilegeCodeRequest request) {
          cardPrivilegeCodeService.create(request);
          return ResponseEntity.ok(ResponseDto.success(null, "Card privilege code created successfully"));
      }

      @PutMapping
      public ResponseEntity<ResponseDto<String>> update(@Valid @RequestBody UpdateCardPrivilegeCodeRequest request) {
          cardPrivilegeCodeService.update(request);
          return ResponseEntity.ok(ResponseDto.success(null, "Card privilege code updated successfully"));
      }

# test sample

    unit test sample (service):
      @Test
      public void createCardPrivilegeCodeSuccess() {
          // arrange valid request + mapper + validator + query save mocks
          // act service.create(request)
          // assert saved entity returned and dependencies invoked
      }

      @Test
      public void createCardPrivilegeCodeFailureValidationError() {
          // arrange validator throws ValidationException
          // act/assert service.create(request) throws ValidationException
          // verify save is never called
      }

      @Test
      public void updateCardPrivilegeCodeSuccess() {
          // arrange active entity lookup + validator update + save mocks
          // act service.update(request)
          // assert updated entity returned and dependencies invoked
      }

      @Test
      public void updateCardPrivilegeCodeFailureValidationError() {
          // arrange validator throws ValidationException
          // act/assert service.update(request) throws ValidationException
          // verify save is never called
      }

    integration test sample (controller):
      @Test
      public void testCreateCardPrivilegeCodeSuccess() {
          // arrange valid request DTO
          // act controller.create(request)
          // assert ResponseDto success and data persisted
      }

      @Test
      public void testCreateCardPrivilegeCodeFailValidation() {
          // arrange invalid payload (e.g. effectiveTo <= effectiveFrom)
          // act/assert controller.create(request) throws ValidationException
      }

      @Test
      public void testUpdateCardPrivilegeCodeSuccess() {
          // arrange existing code via create then call update
          // assert ResponseDto success and persisted values changed
      }

      @Test
      public void testUpdateCardPrivilegeCodeFailValidation() {
          // arrange update request with no updatable fields
          // act/assert controller.update(request) throws ValidationException
      }

